package com.sba301.vaccinex.dto.response;

import com.sba301.vaccinex.pojo.composite.VaccineIntervalId;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VaccineIntervalResponseDTO {
    VaccineIntervalId id;
    VaccineResponseIntervalCustomDTO toVaccine;
    long daysBetween;
}
