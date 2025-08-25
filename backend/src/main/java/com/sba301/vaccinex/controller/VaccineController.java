package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.internal.PagingResponse;
import com.sba301.vaccinex.dto.request.VaccineCreateRequest;
import com.sba301.vaccinex.dto.request.VaccineUpdateRequest;
import com.sba301.vaccinex.dto.response.VaccineResponseDTO;
import com.sba301.vaccinex.exception.BadRequestException;
import com.sba301.vaccinex.exception.ElementExistException;
import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.exception.UnchangedStateException;
import com.sba301.vaccinex.mapper.VaccineMapper;
import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.service.spec.BatchService;
import com.sba301.vaccinex.service.spec.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
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
@RequestMapping("/api/v1/vaccines")
@Tag(name = "Vaccine", description = "Các hoạt động liên quan đến quản lý vaccine")
public class VaccineController {

    private final VaccineService vaccineService;
    private final VaccineMapper vaccineMapper;
    private final BatchService batchService;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    /**
     * Method get all vaccines
     *
     * @param currentPage currentOfThePage
     * @param pageSize    numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all vaccines", description = "Retrieves all vaccines, with optional pagination")
//    @PreAuthorize("hasRole('ADMIN')")
    @PermitAll
    @GetMapping("")
    public ResponseEntity<PagingResponse> getAllVaccines(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = vaccineService.getAllVaccines(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all vaccines
     *
     * @return list or empty
     */
    @Operation(summary = "Get all vaccines not paging", description = "Retrieves all vaccines not paging")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/non-paging")
    public ResponseEntity<ObjectResponse> getAllVaccinesNotPaging(@RequestParam(value = "status", required = false) String status) {
        List<VaccineResponseDTO> results = (status != null && status.equals("active")) ? vaccineService.getVaccinesActiveV2() : vaccineService.getVaccinesV2();
        return !results.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tiêm vắc-xin thành công", results)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tiêm vắc-xin không thành công", null));
    }

    /**
     * Method get all vaccines have status is active
     *
     * @param currentPage currentOfThePage
     * @param pageSize    numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all vaccines active", description = "Retrieves all vaccines have status is active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<PagingResponse> getAllVaccinesActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = vaccineService.getAllVaccineActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method search vaccines active with name, purpose, price, minAge and maxAge
     *
     * @param currentPage currentOfThePage
     * @param pageSize    numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Search vaccines active", description = "Retrieves all vaccines are filtered by name, purpose, price, minAge and maxAge")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PagingResponse> searchVaccines(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                         @RequestParam(value = "purpose", required = false, defaultValue = "") String purpose,
                                                         @RequestParam(value = "price", required = false, defaultValue = "") String price,
                                                         @RequestParam(value = "minAge", required = false) Integer minAge,
                                                         @RequestParam(value = "maxAge", required = false) Integer maxAge,
                                                         @RequestParam(value = "sortBy", required = false) String sortBy) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        int resolvedMinAge = (minAge != null) ? minAge : 0;
        int resolvedMaxAge = (maxAge != null) ? maxAge : 0;

        PagingResponse results = vaccineService.searchVaccines(resolvedCurrentPage, resolvedPageSize, name, purpose, price, resolvedMinAge, resolvedMaxAge, sortBy);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method restore vaccine by vaccine id set delete = false
     *
     * @param vaccineID idOfVaccine
     * @return vaccine or null
     */
    @Operation(summary = "Restore vaccine", description = "Restore vaccine by vaccine id set delete = false")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{vaccine-id}/restore")
    public ResponseEntity<ObjectResponse> unDeleteVaccineByID(@PathVariable("vaccine-id") int vaccineID) {
        try {
            VaccineResponseDTO vaccine = vaccineService.undeleteVaccine(vaccineID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Đã khôi phục vắc-xin thành công", vaccine));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Không khôi phục được vắc-xin. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Không khôi phục được vắc-xin." + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Không khôi phục được vắc-xin.", null));
        }
    }

    /**
     * Method get vaccine by id
     *
     * @param vaccineID idOfVaccine
     * @return vaccine or null
     */
    @Operation(summary = "Get vaccine by id", description = "Get vaccine by id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{vaccine-id}")
    public ResponseEntity<ObjectResponse> getVaccineByID(@PathVariable("vaccine-id") int vaccineID) {
        Vaccine vaccine = vaccineService.findById(vaccineID);
        return vaccine != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tiêm vắc-xin theo ID thành công", vaccineMapper.vaccineToVaccineResponseDTO(vaccine))) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tiêm vắc-xin theo ID không thành công", null));
    }

    /**
     * Method create vaccine
     *
     * @param vaccineCreateRequest param basic for vaccine
     * @return vaccineResponseDTO or null
     */
    @Operation(summary = "Create vaccine", description = "Create vaccine")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ObjectResponse> createVaccine(@Valid @RequestBody VaccineCreateRequest vaccineCreateRequest) {
        try {
            VaccineResponseDTO vaccineResponseDTO = vaccineService.createVaccine(vaccineCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tạo vaccine thành công.", vaccineResponseDTO));
        } catch (BadRequestException e) {
            log.error("Error creating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo vaccine thất bại. " + e.getMessage(), null));
        } catch (ElementExistException e) {
            log.error("Error creating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo vaccine thất bại.. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error creating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo vaccine thất bại.. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo vaccine thất bại.", null));
        }
    }

    /**
     * Method update vaccine by id
     *
     * @param vaccineUpdateRequest param basic update for vaccine
     * @return vaccine or null
     */
    @Operation(summary = "Update vaccine by id", description = "Update vaccine by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{vaccine-id}")
    public ResponseEntity<ObjectResponse> updateVaccine(@PathVariable("vaccine-id") int vaccineID, @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {
        try {
            VaccineResponseDTO vaccineResponseDTO = vaccineService.updateVaccine(vaccineUpdateRequest, vaccineID);
            if (vaccineResponseDTO != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Cập nhật vắc-xin thành công", vaccineResponseDTO));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Cập nhật vắc-xin không thành công. Vắc-xin là null", null));
        } catch (BadRequestException e) {
            log.error("Error updating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Sửa vaccine thất bại." + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Sửa vaccine thất bại." + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Cập nhật vắc-xin không thành công.", null));
        }
    }

    /**
     * Method delete vaccine set deleted = true
     *
     * @param vaccineID idOfVaccine
     * @return vaccine or null
     */
    @Operation(summary = "Delete vaccine by id", description = "Delete vaccine by id and set deleted = true")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{vaccine-id}")
    public ResponseEntity<ObjectResponse> deleteVaccineByID(@PathVariable("vaccine-id") int vaccineID) {
        try {
            VaccineResponseDTO results = vaccineService.deleteVaccine(vaccineID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Xóa vắc-xin thành công", results));
        } catch (ElementNotFoundException e) {
            log.error("Error while deleting vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Xóa vắc-xin không thành công." + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error while deleting vaccine", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Xóa vắc-xin không thành công.", null));
        }
    }

    @GetMapping("/v2")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> getVaccines() {
        return ResponseEntity.ok(
                vaccineService.getVaccines()
        );
    }

    @GetMapping("/quantities")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ObjectResponse> getQuantityByVaccines() {
        return ResponseEntity.ok()
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Số lượng vắc-xin đã thu hồi thành công")
                                .data(batchService.getQuantityOfVaccines())
                                .build()
                );
    }
}
