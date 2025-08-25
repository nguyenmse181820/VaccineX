package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.internal.PagingRequest;
import com.sba301.vaccinex.dto.request.TransactionCreateRequest;
import com.sba301.vaccinex.dto.request.TransactionUpdateRequest;
import com.sba301.vaccinex.pojo.enums.Shift;
import com.sba301.vaccinex.service.spec.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Các hoạt động liên quan đến giao dịch")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<MappingJacksonValue> transactions(
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(required = false, defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(
                transactionService.getAllTransactions(PagingRequest.builder()
                        .pageNo(pageNo)
                        .pageSize(pageSize)
                        .params(params)
                        .filters(filters)
                        .sortBy(sortBy)
                        .build()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> createTransaction(
            @RequestBody TransactionCreateRequest request
    ) {
        transactionService.createTransaction(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.CREATED.toString())
                                .message("Đã tạo một giao dịch")
                                .build()
                );
    }

    @PutMapping("/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> updateTransaction(
            @PathVariable Integer transactionId,
            @RequestBody TransactionUpdateRequest request
    ) {
        transactionService.updateTransaction(transactionId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Giao dịch được cập nhật" + transactionId)
                                .build()
                );
    }

    @DeleteMapping("/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ObjectResponse> deleteTransaction(
            @PathVariable Integer transactionId
    ) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ObjectResponse.builder()
                                .status(HttpStatus.OK.toString())
                                .message("Giao dịch đã xóa")
                                .build()
                );
    }
}
