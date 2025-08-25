package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.draft.VaccineDraftRequest;
import com.sba301.vaccinex.service.spec.VaccineScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Vaccine", description = "Các hoạt động liên quan đến quản lý lịch tiêm")
public class VaccineScheduleController {

    private final VaccineScheduleService vaccineScheduleService;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ObjectResponse> getDoctorSchedule(@PathVariable Integer doctorId, @RequestParam LocalDate date) {

        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Lấy lịch của bác sĩ thành công")
                        .data(vaccineScheduleService.getDoctorSchedule(doctorId, date))
                        .build()
        );
    }

    @GetMapping("/{detailId}")
    public ResponseEntity<ObjectResponse> getScheduleDetails(@PathVariable Integer detailId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Lấy chi tiết lịch thành công")
                        .data(vaccineScheduleService.getScheduleDetails(detailId))
                        .build()
        );
    }

    @PutMapping("/{scheduleId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ObjectResponse> updateTime(
            @PathVariable Integer scheduleId,
            @RequestParam LocalDateTime newDate
    ) {
        vaccineScheduleService.updateSchedule(scheduleId, newDate);
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Cập nhật thời gian thành công")
                        .data(null)
                        .build()
        );
    }

    @PutMapping("/existing/{scheduleId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ObjectResponse> updateExistingSchedule(
            @PathVariable Integer scheduleId,
            @RequestParam LocalDateTime newDate
    ) {
        vaccineScheduleService.updateExistingSchedule(scheduleId, newDate);
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Cập nhật lịch tiêm thành công")
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/draft")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ObjectResponse> draftSchedule(
            @RequestBody VaccineDraftRequest request
    ) {
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Tạo lịch nháp thành công")
                        .data(vaccineScheduleService.draftSchedule(request))
                        .build()
        );
    }

    @PutMapping("/doctor/{doctorId}/confirm/{scheduleId}")
    public ResponseEntity<ObjectResponse> confirmVaccination(@PathVariable Integer scheduleId, @PathVariable Integer doctorId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Xác nhận tiêm chủng thành công")
                        .data(vaccineScheduleService.confirmVaccination(scheduleId, doctorId))
                        .build()
        );
    }

    @GetMapping("doctor/{doctorId}/history")
    public ResponseEntity<ObjectResponse> getHistorySchedules(@PathVariable Integer  doctorId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Lấy lịch sử lịch")
                        .data(vaccineScheduleService.getDoctorHistory(doctorId))
                        .build()
        );
    }

}
