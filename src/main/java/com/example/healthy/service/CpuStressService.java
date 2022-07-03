package com.example.healthy.service;

import org.springframework.stereotype.Service;

@Service
public class CpuStressService {
    public double calculateRangeSum(int l, int r) {
        double sum = 0;
        for (int i = l; i <= r; i++) {
            sum += Math.sqrt(i) / (r - l + 1);
        }
        return sum;
    }
}
