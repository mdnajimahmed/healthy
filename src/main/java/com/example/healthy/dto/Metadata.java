package com.example.healthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Metadata {
    private String instanceId;
    private String privateIp;
    private String publicIp;
    private String region;
    private String availabilityZone;
    private Network network;
}
