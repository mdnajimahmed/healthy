package com.example.healthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Network {
    private String vpc;
    private String subnet;
}
