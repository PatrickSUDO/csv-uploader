package com.psu.repository;

import com.psu.model.DataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DataRecordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DataRecordRepository dataRecordRepository;

    private DataRecord record;

    @BeforeEach
    void setUp() {
        record = new DataRecord();
        record.setCode("271636001");
        record.setDisplayValue("Polsslag regelmatig");

        entityManager.persist(record);
        entityManager.flush();
    }

    @Test
    void whenFindByCode_thenReturnDataRecord() {
        Optional<DataRecord> foundRecord = dataRecordRepository.findByCode(record.getCode());

        assertThat(foundRecord.isPresent()).isTrue();
        assertThat(foundRecord.get().getCode()).isEqualTo(record.getCode());
    }

    @Test
    void whenFindAll_thenReturnAllDataRecords() {
        List<DataRecord> records = dataRecordRepository.findAll();

        assertThat(records).isNotEmpty();
        assertThat(records.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void whenDeleteByCode_thenRecordShouldBeDeleted() {
        dataRecordRepository.delete(record);
        Optional<DataRecord> deletedRecord = dataRecordRepository.findById(record.getId());

        assertThat(deletedRecord.isPresent()).isFalse();
    }
}