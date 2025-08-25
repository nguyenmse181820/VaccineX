package com.sba301.vaccinex.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VaccineStockRequirement {
    private Integer vaccineId;
    private String vaccineCode;
    private String vaccineName;
    private int required;
    private int available;
    private int shortage;
    private boolean isShortage;

    public boolean isShortage() {
        return this.isShortage;
    }
}
