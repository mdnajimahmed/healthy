package com.example.healthy;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.util.EC2MetadataUtils;
import dto.Metadata;
import dto.Network;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
public class IsHealthyController {
    private final InMemoryCache inMemoryCache;

    public IsHealthyController(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @GetMapping("/health")
    public ResponseEntity getHealth() {
        if (inMemoryCache.isHealthy()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/flip")
    public ResponseEntity flipHealth() {
        inMemoryCache.setHealthy(!inMemoryCache.isHealthy());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/meta")
    public ResponseEntity<Metadata> getMetaData() {
        log.info("Fetching meta");
        if (!inMemoryCache.isHealthy()) {
            return ResponseEntity.internalServerError().build();
        }
        if(inMemoryCache.getMetadata()!=null){
            return ResponseEntity.ok(inMemoryCache.getMetadata());
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
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/task-info")
    public ResponseEntity<String> getTaskInfo() {
        String ecsContainerMetadataUri = System.getenv("ECS_CONTAINER_METADATA_URI") + "/task";
        log.info("ecsContainerMetadataUri = {}",ecsContainerMetadataUri);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(ecsContainerMetadataUri, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
