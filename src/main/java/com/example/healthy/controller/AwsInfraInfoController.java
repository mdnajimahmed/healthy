package com.example.healthy.controller;

import com.example.healthy.service.AwsInfraInfoService;
import com.example.healthy.dto.Metadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AwsInfraInfoController {
    private final AwsInfraInfoService awsInfraInfoService;

    public AwsInfraInfoController(AwsInfraInfoService awsInfraInfoService) {
        this.awsInfraInfoService = awsInfraInfoService;
    }

    @GetMapping("/meta")
    public ResponseEntity<Metadata> getMetaData() {
        log.info("Fetching meta");
        Metadata metadata = awsInfraInfoService.extractEc2Details();
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/task-info")
    public ResponseEntity<String> getTaskInfo() {
        String ecsTaskInfo = awsInfraInfoService.extractEcsDetails();
        return ResponseEntity.ok(ecsTaskInfo);
    }
}
