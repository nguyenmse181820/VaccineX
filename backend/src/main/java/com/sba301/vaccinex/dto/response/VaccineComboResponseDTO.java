package com.sba301.vaccinex.dto.response;

import com.sba301.vaccinex.pojo.composite.VaccineComboId;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VaccineComboResponseDTO {
    VaccineComboId id;

    VaccineResponseDTO vaccine;

    ComboResponseDTO combo;

    Long intervalDays;

}
