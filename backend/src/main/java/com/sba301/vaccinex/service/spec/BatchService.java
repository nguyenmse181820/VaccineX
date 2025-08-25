package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.dto.batch.BatchQuantityDTO;
import com.sba301.vaccinex.dto.internal.PagingRequest;
import com.sba301.vaccinex.dto.request.BatchCreateRequest;
import com.sba301.vaccinex.dto.request.BatchUpdateRequest;
import com.sba301.vaccinex.dto.request.VaccineReturnRequest;
import com.sba301.vaccinex.pojo.Batch;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;

public interface BatchService {

    MappingJacksonValue getAllBatches(PagingRequest request);

    Batch getBatchById(Integer id);

    List<BatchQuantityDTO> getQuantityOfVaccines();

    void createBatch(BatchCreateRequest request);

    void updateBatch(Integer batchId, BatchUpdateRequest request);

    void deleteBatch(Integer batchId);

    void returnVaccine(VaccineReturnRequest request);
}
