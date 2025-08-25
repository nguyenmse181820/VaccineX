package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.internal.PagingRequest;
import com.sba301.vaccinex.dto.request.BatchCreateRequest;
import com.sba301.vaccinex.dto.request.BatchUpdateRequest;
import com.sba301.vaccinex.dto.request.VaccineReturnRequest;
import com.sba301.vaccinex.service.spec.BatchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/batches")
@RequiredArgsConstructor
@Tag(name = "Batch", description = "Các hoạt động liên quan đến lô vaccine")
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'DOCTOR')")
    public ResponseEntity<MappingJacksonValue> getAllBatches(
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(required = false, defaultValue = "id") String sortBy
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(batchService.getAllBatches(PagingRequest.builder()
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .params(params)
                        .filters(filters)
                        .sortBy(sortBy)
                        .build()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> createBatch(
            @RequestBody BatchCreateRequest batchCreateRequest
    ) {
        batchService.createBatch(batchCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.CREATED.toString())
                                .message("Tạo batch vaccine thành công")
                                .build()
                );
    }

    @PutMapping("/{batchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> updateBatch(
            @PathVariable Integer batchId,
            @RequestBody BatchUpdateRequest request
    ) {
        batchService.updateBatch(batchId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Chỉnh sửa batch thành công")
                                .build()
                );
    }

    @DeleteMapping("/{batchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> deleteBatch(
            @PathVariable Integer batchId
    ) {
        batchService.deleteBatch(batchId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Xóa batch thành công")
                                .data(null)
                                .build()
                );
    }

    @PutMapping("/returns")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> returnVaccine(
            @RequestBody VaccineReturnRequest request
    ) {
        batchService.returnVaccine(request);
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Trả vaccine dư thành công")
                        .data(null)
                        .build()
        );
    }
}