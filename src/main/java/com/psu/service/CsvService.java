package com.psu.service;

import com.psu.model.DataRecord;
import com.psu.repository.DataRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final Logger log = LoggerFactory.getLogger(CsvService.class);
    @Autowired
    private DataRecordRepository dataRecordRepository;

    public void uploadCsvFile(MultipartFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            List<DataRecord> records = new ArrayList<>();
            for (CSVRecord csvRecord : csvParser) {
                DataRecord record = mapToDataRecord(csvRecord);
                records.add(record);
            }
            dataRecordRepository.saveAll(records);
        } catch (Exception e) {
            log.error("Failed to parse CSV file: ", e);
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }


    private DataRecord mapToDataRecord(CSVRecord csvRecord) {
        DataRecord record = new DataRecord();
        record.setSource(csvRecord.get("source"));
        record.setCodeListCode(csvRecord.get("codeListCode"));
        record.setCode(csvRecord.get("code"));
        record.setDisplayValue(csvRecord.get("displayValue"));
        record.setLongDescription(csvRecord.get("longDescription"));
        try {
            String fromDateStr = csvRecord.get("fromDate").trim();
            String toDateStr = csvRecord.get("toDate").trim();
            record.setFromDate(!fromDateStr.isEmpty() ? dateFormat.parse(fromDateStr) : null);
            record.setToDate(!toDateStr.isEmpty() ? dateFormat.parse(toDateStr) : null);
            if (!csvRecord.get("sortingPriority").isEmpty()) {
                record.setSortingPriority(Integer.parseInt(csvRecord.get("sortingPriority")));
            }
        } catch (Exception e) {
            log.error("Error parsing record: ", e);
            // Handle or rethrow as needed
        }
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
