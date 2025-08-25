package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.request.ChildrenRequestDTO;
import com.sba301.vaccinex.dto.response.ChildrenResponseDTO;
import com.sba301.vaccinex.exception.ParseEnumException;
import com.sba301.vaccinex.service.spec.ChildrenService;
import com.sba301.vaccinex.service.spec.VaccineScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
@Tag(name = "Children", description = "Các hoạt động liên quan đến quản lý trẻ em")
public class ChildrenController {

    private final ChildrenService childService;
    private final VaccineScheduleService vaccineScheduleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ObjectResponse> getAllChildren() {
        List<ChildrenResponseDTO> children = childService.findAll();
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Lấy danh sách tất cả trẻ thành công")
                        .data(children)
                        .build()
        );
    }

    @GetMapping("/{childrenId}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> getChildById(@PathVariable Integer childrenId) {
        ChildrenResponseDTO child = childService.findById(childrenId);
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Lấy chi tiết 1 trẻ thành công")
                        .data(child)
                        .build()
        );
    }

    @GetMapping("/parent")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> getChildrenByParentId(HttpServletRequest request) {

        List<ChildrenResponseDTO> children = childService.findByParentId(request);
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Lấy danh sách trẻ của 1 khách hàng thành công")
                        .data(children)
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> createNewChild(@Valid @RequestBody ChildrenRequestDTO dto, HttpServletRequest request) throws ParseEnumException {
        ChildrenResponseDTO createdChild = childService.createChild(dto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ObjectResponse.builder()
                        .status(HttpStatus.CREATED.toString())
                        .message("Thêm trả vào hệ thống thành công")
                        .data(createdChild)
                        .build()
        );
    }

    @PutMapping("/{childrenId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> updateChild(@PathVariable Integer childrenId, @Valid @RequestBody ChildrenRequestDTO dto, HttpServletRequest request) {
        ChildrenResponseDTO updatedChild = childService.update(childrenId, dto, request);
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Cập nhật thông tin trẻ thành công")
                        .data(updatedChild)
                        .build()
        );
    }

    @DeleteMapping("/{childrenId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> deleteChildById(@PathVariable Integer childrenId) {
        childService.deleteById(childrenId);
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Xóa trẻ thành công")
                        .build()
        );
    }

    @GetMapping("/{childId}/schedules/availability")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ObjectResponse> getEarliestPossibleSchedule(
            @PathVariable Integer childId
    ) {

        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Đã lấy được lịch trình sớm nhất có thể thành công")
                        .data(childService.getEarliestPossibleSchedule(childId))
                        .build()
        );
    }

    @GetMapping("/{childId}/schedules/draft")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ObjectResponse> getDraftSchedules(
            @PathVariable Integer childId
    ) {
        return ResponseEntity.ok(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Đã tải lịch trình dự thảo thành công")
                        .data(vaccineScheduleService.getDrafts(childId))
                        .build()
        );
    }

    @DeleteMapping("/{childId}/schedules/drafts")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ObjectResponse> deleteDraftSchedules(
            @PathVariable Integer childId
    ) {
        vaccineScheduleService.deleteDraftSchedules(childId);
        return ResponseEntity.ok(ObjectResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Đã tải lịch trình dự thảo thành công")
                .data(null)
                .build());
    }
}
