package com.sba301.vaccinex.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class VaccineInventoryAlert {
    private LocalDate date;
    private int daysFromNow;
    private List<VaccineStockRequirement> vaccineRequirements;
    
    // Helper method to check if there are any shortages on this date
    public boolean hasShortages() {
        return vaccineRequirements.stream().anyMatch(VaccineStockRequirement::isShortage);
    }
}
