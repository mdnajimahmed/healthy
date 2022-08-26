package com.example.healthy.service;

import com.example.healthy.dto.K8sPodInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class K8sService {
    public K8sPodInfo getIdentity() {
        String hostname = System.getenv("HOSTNAME");
        String podName = System.getenv("MY_POD_NAME");
        String podNamespace = System.getenv("MY_POD_NAMESPACE");
        String myPodIp = System.getenv("MY_POD_IP");
        String appVersion = System.getenv("COMMIT_SHA_6");
        return new K8sPodInfo(hostname,podName,podNamespace,myPodIp,appVersion);
    }
}
