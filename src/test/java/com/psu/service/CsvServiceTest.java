package com.psu.service;


import com.psu.model.DataRecord;
import com.psu.repository.DataRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class CsvServiceTest {

    @Mock
    private DataRecordRepository dataRecordRepository;

    @InjectMocks
    private CsvService csvService;

    @Captor
    private ArgumentCaptor<List<DataRecord>> recordCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadCsvFile_successfullyParsesAndSavesData() throws Exception {
        String csvContent = "source,codeListCode,code,displayValue,longDescription,fromDate,toDate,sortingPriority\n" +
                "ZIB,ZIB001,271636001,Polsslag regelmatig,The long description is necessary,01-01-2019,,1";
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        csvService.uploadCsvFile(file);

        verify(dataRecordRepository).saveAll(recordCaptor.capture());
        List<DataRecord> capturedRecords = recordCaptor.getValue();
        assertThat(capturedRecords).hasSize(1);
        DataRecord record = capturedRecords.get(0);
        assertThat(record.getSource()).isEqualTo("ZIB");
        assertThat(record.getCode()).isEqualTo("271636001");
    }

    @Test
    void findAll_returnsAllDataRecords() {
        when(dataRecordRepository.findAll()).thenReturn(Collections.singletonList(new DataRecord()));

        List<DataRecord> records = csvService.findAll();

        assertThat(records).hasSize(1);
        verify(dataRecordRepository).findAll();
    }

    @Test
    void findByCode_returnsMatchingRecord() {
        DataRecord expectedRecord = new DataRecord();
        expectedRecord.setCode("271636001");
        when(dataRecordRepository.findByCode(anyString())).thenReturn(Optional.of(expectedRecord));

        DataRecord result = csvService.findByCode("271636001");

        assertThat(result).isEqualTo(expectedRecord);
        verify(dataRecordRepository).findByCode("271636001");
    }

    @Test
    void deleteAll_deletesAllRecords() {
        csvService.deleteAll();

        verify(dataRecordRepository).deleteAll();

        List<DataRecord> records = csvService.findAll();
        assertThat(records).hasSize(0);
    }

    @Test
    void findByCode_whenRecordDoesNotExist_throwsEntityNotFoundException() {
        String nonExistentCode = "nonexistent";
        when(dataRecordRepository.findByCode(nonExistentCode)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            csvService.findByCode(nonExistentCode);
        });

        assertThat(exception.getMessage()).contains("The DataRecord not found with code: " + nonExistentCode);
    }
}