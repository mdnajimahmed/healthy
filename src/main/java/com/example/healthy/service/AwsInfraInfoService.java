package com.example.healthy.service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.util.EC2MetadataUtils;
import com.example.healthy.repository.InMemoryCache;
import com.example.healthy.dto.Metadata;
import com.example.healthy.dto.Network;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class AwsInfraInfoService {
    private final InMemoryCache inMemoryCache;

    public AwsInfraInfoService(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    public Metadata extractEc2Details() {
        if (inMemoryCache.getMetadata() != null) {
            return inMemoryCache.getMetadata();
        }
        // Resolve the instanceId
        String instanceId = EC2MetadataUtils.getInstanceId();

        // Resolve (first/primary) private IP
        String privateAddress = EC2MetadataUtils.getInstanceInfo().getPrivateIp();

        // Resolve public IP
        AmazonEC2 client = AmazonEC2ClientBuilder.defaultClient();
        String publicAddress = client.describeInstances(new DescribeInstancesRequest()
                        .withInstanceIds(instanceId))
                .getReservations()
                .stream()
                .map(Reservation::getInstances)
                .flatMap(List::stream)
                .findFirst()
                .map(Instance::getPublicIpAddress)
                .orElse(null);
        String ec2InstanceRegion = EC2MetadataUtils.getEC2InstanceRegion();
        String availabilityZone = EC2MetadataUtils.getAvailabilityZone();
        Network network = EC2MetadataUtils.getNetworkInterfaces().stream().map(n -> {
            String vpcId = n.getVpcId();
            String subnetId = n.getSubnetId();
            return new Network(vpcId, subnetId);
        }).findFirst().orElse(null);
        Metadata metadata = new Metadata(instanceId, privateAddress, publicAddress, ec2InstanceRegion, availabilityZone, network);
        inMemoryCache.setMetadata(metadata);
        return metadata;
    }

    public String extractEcsDetails() {
        String ecsContainerMetadataUri = System.getenv("ECS_CONTAINER_METADATA_URI") + "/task";
        log.info("ecsContainerMetadataUri = {}", ecsContainerMetadataUri);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(ecsContainerMetadataUri, String.class);
        return response.getBody();
    }
}
