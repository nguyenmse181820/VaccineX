package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.service.spec.AppointmentVerificationService;
import com.sba301.vaccinex.dto.response.VaccineInventoryAlert;
import com.sba301.vaccinex.service.spec.VaccineInventoryNotificationService;
import com.sba301.vaccinex.dto.internal.ObjectResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Các hoạt động liên quan đến quản lý kho")
public class InventoryNotificationController {

    private final AppointmentVerificationService appointmentVerificationService;
    private final VaccineInventoryNotificationService inventoryNotificationService;

    @GetMapping("/verify-appointment")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ObjectResponse> verifyAppointmentAvailability(
            @RequestParam Integer vaccineId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  LocalDateTime appointmentDate) {

        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Xác minh cuộc hẹn đã hoàn tất")
                        .data(appointmentVerificationService.verifyAppointmentAvailability(vaccineId, appointmentDate))
                        .build()
        );
    }

    @GetMapping("/vaccine-alerts")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ObjectResponse> getVaccineInventoryAlerts(@RequestParam(required = false) Integer days) {
        List<VaccineInventoryAlert> alerts = inventoryNotificationService.getVaccineInventoryAlerts(days);

        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Cảnh báo về hàng tồn kho vắc-xin đã được tạo thành công")
                        .data(alerts)
                        .build()
        );
    }
}