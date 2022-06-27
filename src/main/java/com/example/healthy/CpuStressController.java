package com.example.healthy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CpuStressController {
    @GetMapping("/cpu-stress")
    public ResponseEntity<Double> getSum(@RequestParam("l") int l, @RequestParam("r") int r) {
        double sum = 0;
        for (int i = l; i <= r; i++) {
            sum += Math.sqrt(r - l + 1) / (r - l + 1);
        }
        return ResponseEntity.ok(sum);
    }
}

