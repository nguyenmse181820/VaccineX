package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.service.spec.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Các hoạt động liên quan đến thông báo")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ObjectResponse> getNotifications(
            @PathVariable Integer userId
    ) {
        System.out.println("HELLO");
        return ResponseEntity.status(HttpStatus.OK).body(
                ObjectResponse.builder()
                        .status(HttpStatus.OK.toString())
                        .message("Get notifications")
                        .data(notificationService.getNotificationsByUserId(userId))
                        .build()
        );
    }

}
