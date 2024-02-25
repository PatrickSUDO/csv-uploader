package com.psu.service;

import com.psu.model.DataRecord;
import com.psu.repository.DataRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvService {

    @Autowired
    private DataRecordRepository dataRecordRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private final Logger log = LoggerFactory.getLogger(CsvService.class);

    public void uploadCsvFile(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<DataRecord> records = fileReader.lines().skip(1)
                    .map(this::mapToDataRecord)
                    .collect(Collectors.toList());

            dataRecordRepository.saveAll(records);
        } catch (Exception e) {
            log.error("Failed to parse CSV file: ", e);
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    private DataRecord mapToDataRecord(String line) {
        String[] columns = line.split(",", -1); // Handle missing values
        DataRecord record = new DataRecord();
        record.setSource(columns[0]);
        record.setCodeListCode(columns[1]);
        record.setCode(columns[2]);
        record.setDisplayValue(columns[3]);
        record.setLongDescription(columns[4]);
        try {
            record.setFromDate(!columns[5].isEmpty() ? dateFormat.parse(columns[5]) : null);
            record.setToDate(!columns[6].isEmpty() ? dateFormat.parse(columns[6]) : null);
        } catch (Exception e) {
            log.error("Error parsing date: ", e);
            throw new RuntimeException("Error parsing date: " + e.getMessage());
        }
        record.setSortingPriority(!columns[7].isEmpty() ? Integer.parseInt(columns[7]) : null);
        return record;
    }

    public List<DataRecord> findAll() {
        return dataRecordRepository.findAll();
    }

    public DataRecord findByCode(String code) {
        return dataRecordRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("The DataRecord not found with code: " + code));
    }

    public void deleteAll() {
        dataRecordRepository.deleteAll();
        log.info("All records deleted successfully.");
    }
}
