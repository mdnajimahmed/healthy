package com.example.healthy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class K8sPodInfo {
    private final String hostname;
    private final String podName;
    private final String podNamespace;
    private final String myPodIp;
}
