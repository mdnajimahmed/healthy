package com.example.healthy.controller;

import com.example.healthy.service.CpuStressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CpuStressController {
    private final CpuStressService cpuStressService;

    public CpuStressController(CpuStressService cpuStressService) {
        this.cpuStressService = cpuStressService;
    }

    @GetMapping("/cpu-stress")
    public ResponseEntity<Double> getSum(@RequestParam("l") int l, @RequestParam("r") int r) {
        log.info("Calculating range sum from {} to {}",l,r);
        double sum = cpuStressService.calculateRangeSum(l,r);
        return ResponseEntity.ok(sum);
    }
}

