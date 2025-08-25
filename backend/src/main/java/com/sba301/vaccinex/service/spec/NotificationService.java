package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.dto.response.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsByUserId(Integer id);
}
