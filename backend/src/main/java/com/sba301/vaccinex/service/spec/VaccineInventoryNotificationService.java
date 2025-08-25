package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.dto.response.VaccineInventoryAlert;

import java.util.List;

public interface VaccineInventoryNotificationService {
    List<VaccineInventoryAlert> getVaccineInventoryAlerts(Integer days);
}
