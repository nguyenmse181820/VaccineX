package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.request.ExportVaccineRequest;
import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.exception.BadRequestException;
import com.sba301.vaccinex.service.spec.WarehouseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse", description = "Các hoạt động liên quan đến quản lý kho")
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Generate a report on the required vaccine doses for the morning and afternoon shifts.
     */
    @GetMapping("/reports")
    @PreAuthorize("hasRole('STAFF')")
    @PermitAll
    public ResponseEntity<ObjectResponse> getVaccineReports(@RequestParam Integer doctorId, @RequestParam String shift, @RequestParam LocalDate date) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Báo cáo số lượng vắc-xin cho ca sáng/chiều")
                        .data(warehouseService.getVaccineReports(doctorId, shift, date))
                        .build()
        );
    }

    /**
     * Create a vaccine export request (Deduct from batch → Create transaction).
     */
    @PostMapping("/export")
    @PreAuthorize("hasRole('STAFF')")
    @PermitAll
    public ResponseEntity<ObjectResponse> requestVaccineExport(@RequestBody ExportVaccineRequest request) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ObjectResponse.builder()
                        .status(HttpStatus.CREATED.toString())
                        .message("Yêu cầu xuất khẩu vắc-xin đã được tạo thành công")
                        .data(warehouseService.requestVaccineExport(request))
                        .build()
        );
    }

//    @PostMapping("/return/{batchId}")
//    @PreAuthorize("hasRole('STAFF')")
//    @PermitAll
//    public ResponseEntity<ObjectResponse> returnExcessVaccines(@RequestBody ReturnVaccineRequest request) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                ObjectResponse.builder()
//                        .status(HttpStatus.OK.toString())
//                        .message("Excess vaccines successfully returned to the warehouse")
//                        .data(warehouseService.returnExcessVaccines(batchId, quantity, userId))
//                        .build()
//        );
//    }
}
