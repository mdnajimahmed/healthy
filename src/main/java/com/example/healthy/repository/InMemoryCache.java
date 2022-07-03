package com.example.healthy.repository;

import com.example.healthy.dto.Metadata;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class InMemoryCache {
    private boolean isHealthy = true;
    private Metadata metadata;
}
