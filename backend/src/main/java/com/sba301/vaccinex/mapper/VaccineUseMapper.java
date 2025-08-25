package com.sba301.vaccinex.mapper;

import com.sba301.vaccinex.dto.response.VaccineUseResponseDTO;
import com.sba301.vaccinex.pojo.VaccineUse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VaccineUseMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "deleted", target = "deleted")
    VaccineUseResponseDTO vaccineUseToVaccineUseResponseDTO(VaccineUse vaccineUse);

    @Mapping(source = "id", target = "id")
    VaccineUse vaccineUseResponseDTOToVaccineUse(VaccineUseResponseDTO vaccineUseResponseDTO);

}
