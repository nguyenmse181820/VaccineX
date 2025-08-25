package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.request.VaccineUseUpdateRequest;
import com.sba301.vaccinex.dto.request.VaccineUseCreateRequest;
import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.internal.PagingResponse;
import com.sba301.vaccinex.dto.response.VaccineResponseDTO;
import com.sba301.vaccinex.dto.response.VaccineUseResponseDTO;
import com.sba301.vaccinex.exception.ElementExistException;
import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.exception.UnchangedStateException;
import com.sba301.vaccinex.mapper.VaccineUseMapper;
import com.sba301.vaccinex.pojo.VaccineUse;
import com.sba301.vaccinex.service.spec.VaccineUseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purposes")
@Tag(name = "Purpose", description = "Các hoạt động liên quan đến công dụng")
public class VaccineUseController {

    private final VaccineUseService vaccineUseService;
    private final VaccineUseMapper vaccineUseMapper;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    /**
     * Method get all purposes
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all purposes", description = "Retrieves all purposes, with optional pagination")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<PagingResponse> getAllPurposes(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = vaccineUseService.getAllPurposes(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all purposes have status is active
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all purposes active", description = "Retrieves all purposes have status is active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<PagingResponse> getAllPurposesActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = vaccineUseService.getAllPurposesActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all purposes non - paging
     *
     * @return list or empty
     */
    @Operation(summary = "Get all purposes not paging", description = "Retrieves all purposes not paging")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/non-paging")
    public ResponseEntity<ObjectResponse> getAllPurposesNotPaging(@RequestParam(value = "status", required = false) String status) {
        List<VaccineUseResponseDTO> results = (status != null && status.equals("active")) ? vaccineUseService.getPurposesActive() : vaccineUseService.getPurposes();
        return !results.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy được tất cả các tác dụng thành công", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Lấy được tất cả các tác dụng thất bại", null));
    }

    /**
     * Method search vaccines use with name and sortBy
     *
     * @param currentPage currentOfThePage
     * @param pageSize    numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Search vaccineUses active", description = "Retrieves all vaccineUses are filtered by name and sortBy")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PagingResponse> searchVaccines(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                         @RequestParam(value = "sortBy", required = false) String sortBy) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;

        PagingResponse results = vaccineUseService.searchVaccineUses(resolvedCurrentPage, resolvedPageSize, name, sortBy);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method restore purpose by vaccine id set delete = false
     *
     * @param purposeID idOfPurpose
     * @return purpose or null
     */
    @Operation(summary = "Restore purpose", description = "Restore purpose by purpose id set delete = false")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{purpose-id}/restore")
    public ResponseEntity<ObjectResponse> unDeletePurposeByID(@PathVariable("purpose-id") int purposeID) {
        try {
            VaccineUseResponseDTO result = vaccineUseService.undeletePurpose(purposeID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Đã khôi phục mục đích thành công", result));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Mục đích khôi phục không thành công. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Mục đích khôi phục không thành công. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Mục đích khôi phục không thành công.", null));
        }
    }

    /**
     * Method get purpose by id
     *
     * @param purposeID idOfPurpose
     * @return purpose or null
     */
    @Operation(summary = "Get purpose by id", description = "Get purpose by id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{purpose-id}")
    public ResponseEntity<ObjectResponse> getPurposeByID(@PathVariable("purpose-id") int purposeID) {
        VaccineUse vaccineUse = vaccineUseService.findById(purposeID);
        return vaccineUse != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy mục đích bằng ID thành công", vaccineUseMapper.vaccineUseToVaccineUseResponseDTO(vaccineUse))) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Lấy mục đích theo ID không thành công", null));
    }

    /**
     * Method create purpose
     *
     * @param vaccineUseCreateRequest param basic for purpose
     * @return purposeResponseDTO or null
     */
    @Operation(summary = "Create purpose", description = "Create purpose")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ObjectResponse> createPurpose(@Valid @RequestBody VaccineUseCreateRequest vaccineUseCreateRequest) {
        try {
            VaccineUseResponseDTO result = vaccineUseService.createPurpose(vaccineUseCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tạo mục đích thành công", result));
        } catch (ElementExistException e) {
            log.error("Error creating purpose", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo mục đích không thành công" + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating purpose", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo mục đích không thành công", null));
        }
    }

    /**
     * Method update purpose by id
     *
     * @param vaccineUseUpdateRequest param basic update for purpose
     * @param purposeID idOfPurpose
     * @return purpose or null
     */
    @Operation(summary = "Update purpose by id", description = "Update purpose by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{purpose-id}")
    public ResponseEntity<ObjectResponse> updatePurpose(@PathVariable("purpose-id") int purposeID, @RequestBody VaccineUseUpdateRequest vaccineUseUpdateRequest) {
        try {
            VaccineUseResponseDTO purposeResponseDTO = vaccineUseService.updatePurpose(vaccineUseUpdateRequest, purposeID);
            if (purposeResponseDTO != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Thành công", "Cập nhật mục đích thành công", purposeResponseDTO));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Thất bại", "Cập nhật mục đích không thành công. Mục đích là null", null));
        } catch (ElementExistException e) {
            log.error("Lỗi khi cập nhật mục đích", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Thất bại", "Cập nhật mục đích không thành công. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Lỗi khi cập nhật mục đích", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Thất bại", "Cập nhật mục đích không thành công. Không tìm thấy mục đích", null));
        } catch (Exception e) {
            log.error("Lỗi cập nhật mục đích", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Thất bại", "Cập nhật mục đích không thành công", null));
        }
    }

    /**
     * Method delete purpose set deleted = true
     *
     * @param purposeID idOfPurpose
     * @return purpose or null
     */
    @Operation(summary = "Delete purpose by id", description = "Delete purpose by id and set deleted = true")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{purpose-id}")
    public ResponseEntity<ObjectResponse> deletePurposeByID(@PathVariable("purpose-id") int purposeID) {
        try {
            VaccineUseResponseDTO results = vaccineUseService.deletePurpose(purposeID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Thành công", "Xóa mục đích thành công", results));
        } catch (ElementNotFoundException e) {
            log.error("Lỗi khi xóa mục đích", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Thất bại", "Xóa mục đích không thành công. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Lỗi khi xóa mục đích", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Thất bại", "Xóa mục đích không thành công", null));
        }
    }

}
