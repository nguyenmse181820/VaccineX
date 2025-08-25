package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.request.OrderRequest;
import com.sba301.vaccinex.service.spec.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Các hoạt động liên quan đến quản lý đơn hàng")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> deposit(
            HttpServletRequest http,
            @RequestBody OrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request, http));
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ObjectResponse> cancel(@PathVariable Integer orderId) {
        return ResponseEntity
                .ok()
                .body(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Đơn hàng đã được hủy thành công.")
                        .data(orderService.refundOrder(orderId))
                        .build()
                );
    }

    @GetMapping("/{orderId}/refund/amount")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ObjectResponse> calculateRefundAmount(@PathVariable Integer orderId) {
        return ResponseEntity
                .ok()
                .body(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Số tiền hoàn lại được tính toán.")
                        .data(orderService.calculateRefundAmount(orderId))
                        .build()
                );
    }

    @GetMapping("/history/{customerId}")
    public ResponseEntity<ObjectResponse> getOrderHistory(@PathVariable Integer customerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Đã lấy lại lịch sử đơn hàng.")
                        .data(orderService.getOrdersWithSchedulesByCustomerId(customerId))
                        .build()
                );
    }

    @GetMapping("/summary")
    public ResponseEntity<ObjectResponse> getOrderSummary() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Order summary retrieved.")
                        .data(orderService.getOrderSummary())
                .build()
                );
    }

    @GetMapping("/revenue")
    public ResponseEntity<ObjectResponse> getRevenue() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Revenue retrieved.")
                        .data(orderService.getRevenue())
                        .build()
                );
    }

}
