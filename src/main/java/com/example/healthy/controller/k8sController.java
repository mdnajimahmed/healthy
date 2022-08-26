package com.example.healthy.controller;

import com.example.healthy.dto.K8sPodInfo;
import com.example.healthy.service.K8sService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class k8sController {
    private final K8sService k8sService;

    public k8sController(K8sService k8sService) {
        this.k8sService = k8sService;
    }

    @GetMapping("/k8s-identity")
    public ResponseEntity<K8sPodInfo> getIdentity() {
        return ResponseEntity.ok(k8sService.getIdentity());
    }
}
