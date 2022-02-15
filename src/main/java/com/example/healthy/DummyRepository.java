package com.example.healthy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DummyRepository extends JpaRepository<DummyEntity,Long> {
    @Query(value = "select get_pg_info()",nativeQuery = true)
    String runQuery();
}
