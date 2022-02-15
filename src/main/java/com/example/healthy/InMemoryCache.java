package com.example.healthy;

import dto.Metadata;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class InMemoryCache {
    private boolean isHealthy = true;
    private Metadata metadata;
}
