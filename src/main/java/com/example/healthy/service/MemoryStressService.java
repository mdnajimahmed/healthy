package com.example.healthy.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryStressService {
    List<Integer> nonsenseMemory = new ArrayList<>();
    private final int NO_OF_INT_PER_MB = 1 << 18;

    public Integer generateNumber(int mb) {
        for (int i = 0; i < NO_OF_INT_PER_MB * mb; i++) {
            nonsenseMemory.add(i);
        }
        return (nonsenseMemory.size()) / NO_OF_INT_PER_MB;
    }
}
