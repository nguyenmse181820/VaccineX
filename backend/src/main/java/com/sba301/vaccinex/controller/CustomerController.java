package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.request.CustomerUpdateProfile;
import com.sba301.vaccinex.dto.internal.PagingRequest;
import com.sba301.vaccinex.dto.request.FeedbackRequestDTO;
import com.sba301.vaccinex.dto.request.ReactionCreateRequest;
import com.sba301.vaccinex.dto.response.ChildrenResponseDTO;
import com.sba301.vaccinex.dto.response.CustomerInfoResponse;
import com.sba301.vaccinex.service.spec.CustomerService;
import com.sba301.vaccinex.service.spec.VaccineScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Các hoạt động liên quan đến quản lý khách hàng")
public class CustomerController {

    private final CustomerService parentService;
    private final VaccineScheduleService vaccineScheduleService;

    @GetMapping("{customerId}/children")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ChildrenResponseDTO>> getChildrenByParentId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(parentService.getChildByParentId(customerId));
    }

    @PutMapping("/schedules/{scheduleId}/feedback")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse> updateFeedback(
            @PathVariable Integer scheduleId,
            @RequestBody FeedbackRequestDTO feedbackRequestDTO
    ) {
        parentService.updateFeedback(feedbackRequestDTO, scheduleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .build()
                );
    }

    @PostMapping("/schedules/{scheduleId}/reaction")
    @PreAuthorize("hasAnyRole('DOCTOR','USER')")
    public ResponseEntity<ObjectResponse> createReaction(
            @PathVariable Integer scheduleId,
            @RequestBody ReactionCreateRequest reactionCreateRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.CREATED.toString())
                                .data(parentService.createReactionDetail(reactionCreateRequest, scheduleId))
                                .build()
                );
    }

    @GetMapping("/{customerId}/schedules")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> schedules(
            @PathVariable Integer customerId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        vaccineScheduleService.getVaccinesByCustomer(customerId,
                                PagingRequest.builder()
                                        .pageNo(pageNo)
                                        .pageSize(pageSize)
                                        .sortBy(sortBy)
                                        .build()
                        ));
    }


    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<ObjectResponse> getCustomerById(@PathVariable Integer id) {
        CustomerInfoResponse user = parentService.findUserById(id);
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Đã lấy được khách hàng thành công")
                        .data(user)
                        .build()
        );
    }

    @PutMapping("/{customerId}")
    @PermitAll
    public ResponseEntity<ObjectResponse> updateCustomer(
            @PathVariable Integer customerId,
            @RequestBody CustomerUpdateProfile request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Khách hàng đã cập nhật thành công")
                                .data(parentService.updateCustomer(customerId, request))
                                .build()
                );
    }
}
