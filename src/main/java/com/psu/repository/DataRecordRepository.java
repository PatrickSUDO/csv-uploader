package com.psu.repository;


import com.psu.model.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataRecordRepository extends JpaRepository<DataRecord, Long> {
    Optional<DataRecord> findByCode(String code);
    // No need to write any CRUD operations, findAll(), findById(), save(), deleteById() are provided automatically.
    // You can define custom query methods here if needed.
}
