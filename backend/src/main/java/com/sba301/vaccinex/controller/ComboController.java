package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.internal.PagingRequest;
import com.sba301.vaccinex.dto.internal.PagingResponse;
import com.sba301.vaccinex.dto.request.VaccineComboCreateRequest;
import com.sba301.vaccinex.dto.request.VaccineComboUpdateRequest;
import com.sba301.vaccinex.dto.response.ComboResponseDTO;
import com.sba301.vaccinex.exception.BadRequestException;
import com.sba301.vaccinex.exception.ElementExistException;
import com.sba301.vaccinex.exception.ElementNotFoundException;
import com.sba301.vaccinex.exception.UnchangedStateException;
import com.sba301.vaccinex.mapper.old.ComboMapper;
import com.sba301.vaccinex.pojo.Combo;
import com.sba301.vaccinex.service.spec.ComboService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/combos")
@Tag(name = "Combo", description = "Các hoạt động liên quan đến quản lý combo")
public class ComboController {

    private final ComboService comboService;
    private final ComboMapper comboMapper;

    @Value("${application.default-current-page}")
    private int defaultCurrentPage;

    @Value("${application.default-page-size}")
    private int defaultPageSize;

    /**
     * Method get all combos
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all combos", description = "Retrieves all combos, with optional pagination")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<PagingResponse> getAllCombos(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = comboService.getAllCombos(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method get all combos have status is active
     *
     * @param currentPage currentOfThePage
     * @param pageSize numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Get all combos active", description = "Retrieves all combos have status is active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<PagingResponse> getAllCombosActive(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        int resolvedCurrentPage = (currentPage != null) ? currentPage : defaultCurrentPage;
        int resolvedPageSize = (pageSize != null) ? pageSize : defaultPageSize;
        PagingResponse results = comboService.getAllCombosActive(resolvedCurrentPage, resolvedPageSize);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method search vaccineCombos active with name, price, minAge and maxAge and sortBy
     *
     * @param currentPage currentOfThePage
     * @param pageSize    numberOfElement
     * @return list or empty
     */
    @Operation(summary = "Search vaccineCombos active", description = "Retrieves all vaccineCombos are filtered by name, price, minAge and maxAge and sortBy")
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

        PagingResponse results = comboService.searchVaccineCombos(resolvedCurrentPage, resolvedPageSize, name, price, resolvedMinAge, resolvedMaxAge, sortBy);
        List<?> data = (List<?>) results.getData();
        return ResponseEntity.status(!data.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(results);
    }

    /**
     * Method restore combo by combo id set delete = false
     *
     * @param comboID idOfCombo
     * @return combo or null
     */
    @Operation(summary = "Restore combo", description = "Restore combo by combo id set delete = false")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{combo-id}/restore")
    public ResponseEntity<ObjectResponse> unDeleteComboByID(@PathVariable("combo-id") int comboID) {
        try {
            ComboResponseDTO combo = comboService.undeleteCombo(comboID);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Undelete combo successfully", combo));
        } catch (ElementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete combo failed. " + e.getMessage(), null));
        } catch (UnchangedStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete combo failed. " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Undelete combo failed", null));
        }
    }

    /**
     * Method get combo by id
     *
     * @param comboID idOfCombo
     * @return combo or null
     */
    @Operation(summary = "Get combo by id", description = "Get combo by id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{combo-id}")
    public ResponseEntity<ObjectResponse> getComboByID(@PathVariable("combo-id") int comboID) {
        Combo combo = comboService.findById(comboID);
        return combo != null ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get combo by ID successfully", comboMapper.comboToComboResponseDTO(combo))) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Get combo by ID failed", null));
    }

    /**
     * Method create combo
     *
     * @param vaccineComboCreateRequest param basic for combo
     * @return comboResponseDTO or null
     */
    @Operation(summary = "Create combo", description = "Create combo")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<ObjectResponse> createCombo(@Valid @RequestBody VaccineComboCreateRequest vaccineComboCreateRequest) {
        try {
            ComboResponseDTO comboResponseDTO = comboService.createCombo(vaccineComboCreateRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tạo gói tiêm thành công", comboResponseDTO));
        } catch (BadRequestException e) {
            log.error("Error creating combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo gói tiêm thất bại. " + e.getMessage(), null));
        } catch (ElementExistException e) {
            log.error("Error creating combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo gói tiêm thất bại. " + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error creating combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Tạo gói tiêm thất bại", null));
        }
    }

    /**
     * Method update combo by id
     *
     * @param vaccineComboUpdateRequest param basic update for combo
     * @param comboID idOfCombo
     * @return combo or null
     */
    @Operation(summary = "Update combo by id", description = "Update combo by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{combo-id}")
    public ResponseEntity<ObjectResponse> updateCombo(@PathVariable("combo-id") int comboID, @RequestBody VaccineComboUpdateRequest vaccineComboUpdateRequest) {
        try {
            ComboResponseDTO comboResponseDTO = comboService.updateCombo(vaccineComboUpdateRequest, comboID);
            if (comboResponseDTO != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Sửa gói tiêm thành công", comboResponseDTO));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Sửa gói tiêm thất bại. Gói tiêm không có giá trị", null));
        } catch (BadRequestException e) {
            log.error("Error updating combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Sửa gói tiêm thất bại. " + e.getMessage(), null));
        } catch (ElementNotFoundException e) {
            log.error("Error while updating combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Sửa gói tiêm thất bại." + e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error updating combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Sửa gói tiêm thất bại", null));
        }
    }

    /**
     * Method delete combo set deleted = true
     *
     * @param comboID idOfCombo
     * @return combo or null
     */
    @Operation(summary = "Delete combo by id", description = "Delete combo by id and set deleted = true")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{combo-id}")
    public ResponseEntity<ObjectResponse> deleteComboByID(@PathVariable("combo-id") int comboID) {
        try {
            Combo combo = comboService.findById(comboID);
            if(combo != null) {
                combo.setDeleted(true);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete combo successfully", comboMapper.comboToComboResponseDTO(comboService.save(combo))));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete combo failed", null));
        } catch (Exception e) {
            log.error("Error deleting combo", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Fail", "Delete combo failed", null));
        }
    }

    @GetMapping("/v2")
    @PreAuthorize("hasAnyRole('USER','DOCTOR','ADMIN')")
    public ResponseEntity<MappingJacksonValue> getAllCombos(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(required = false, defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(
                comboService.getAllCombosV2(PagingRequest.builder()
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .params(params)
                        .sortBy(sortBy)
                        .build())
        );
    }

}
