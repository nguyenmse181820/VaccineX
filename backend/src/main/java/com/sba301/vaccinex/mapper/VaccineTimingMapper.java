package com.sba301.vaccinex.mapper;

import com.sba301.vaccinex.dto.response.VaccineTimingResponseDTO;
import com.sba301.vaccinex.pojo.VaccineTiming;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccineTimingMapper {

    @Mapping(source = "intervalDays", target = "daysAfterPreviousDose")
    VaccineTimingResponseDTO vaccineTimingToVaccineTimingResponseDTO(VaccineTiming vaccineTiming);

}
