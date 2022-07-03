package com.example.healthy.service;

import com.example.healthy.repository.SlowDbQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class SlowDbQueryService {
    private final SlowDbQueryRepository slowDbQueryRepository;

    public SlowDbQueryService(SlowDbQueryRepository dummyRepository) {
        this.slowDbQueryRepository = dummyRepository;
    }

    public String runQuery() {
        return slowDbQueryRepository.runQuery();
    }
}
