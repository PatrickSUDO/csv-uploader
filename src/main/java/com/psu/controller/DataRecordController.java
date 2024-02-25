package com.psu.controller;

import com.psu.model.DataRecord;
import com.psu.service.CsvService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/api/data")
public class DataRecordController {

    @Autowired
    private CsvService csvService;

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        csvService.uploadCsvFile(file);
        return ResponseEntity.ok("CSV file uploaded successfully");
    }

    @GetMapping
    public List<DataRecord> getAllRecords() {
        return csvService.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<DataRecord> getRecordByCode(@PathVariable String code) {
        DataRecord record = csvService.findByCode(code);
        return ResponseEntity.ok(record);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllRecords() {
        csvService.deleteAll();
        return ResponseEntity.ok("All records deleted successfully");
    }
}