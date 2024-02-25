package com.psu.com.psu;

import com.psu.controller.DataRecordController;
import com.psu.model.DataRecord;
import com.psu.service.CsvService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataRecordController.class)
class DataRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CsvService csvService;

    private DataRecord record;

    @BeforeEach
    void setUp() {
        record = new DataRecord();
        record.setCode("271636001");
        record.setDisplayValue("Polsslag regelmatig");
    }

    @Test
    void uploadCsvFile_ShouldReturnSuccessMessage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/csv", "content".getBytes());
        mockMvc.perform(multipart("/api/data/upload-csv").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("CSV file uploaded successfully"));
    }

    @Test
    void getAllRecords_ShouldReturnAllRecords() throws Exception {
        List<DataRecord> records = Arrays.asList(record);
        given(csvService.findAll()).willReturn(records);

        mockMvc.perform(get("/api/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value(records.get(0).getCode()));
    }

    @Test
    void getRecordByCode_ShouldReturnRecord() throws Exception {
        given(csvService.findByCode(record.getCode())).willReturn(record);

        mockMvc.perform(get("/api/data/{code}", record.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(record.getCode()));
    }

    @Test
    void getRecordByCode_NotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException("The DataRecord not found with code: " + record.getCode())).when(csvService).findByCode(record.getCode());

        mockMvc.perform(get("/api/data/{code}", record.getCode()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("The DataRecord not found with code")));
    }

    @Test
    void deleteAllRecords_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(delete("/api/data/delete-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("All records deleted successfully"));
    }

}
