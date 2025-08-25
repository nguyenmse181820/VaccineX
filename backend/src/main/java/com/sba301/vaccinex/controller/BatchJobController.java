package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.service.batchjob.BatchJobService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/batch-jobs")
@Tag(name = "Batch Job", description = "Các hoạt động liên quan đến batch job")
public class BatchJobController {

    private final BatchJobService batchJobService;

    @GetMapping("/reminders")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> sendMail(
    ) {
        batchJobService.remindVaccineSchedules();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/batch-assignment")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ObjectResponse> getBatchByBatchId() {
        batchJobService.assignBatchToSchedules();
        return ResponseEntity.ok().build();
    }

}
