package com.sba301.vaccinex.pojo;

import com.sba301.vaccinex.pojo.composite.VaccineComboId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VaccineCombo {
    @EmbeddedId
    VaccineComboId id;

    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    @MapsId("comboId")
    Combo combo;

    @ManyToOne
    @JoinColumn(name = "vaccine_id", nullable = false)
    @MapsId("vaccineId")
    Vaccine vaccine;

    Long intervalDays;
}
