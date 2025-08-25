package com.sba301.vaccinex.mapper;

import com.sba301.vaccinex.dto.request.RoleRequestDTO;
import com.sba301.vaccinex.dto.response.RoleResponseDTO;
import com.sba301.vaccinex.pojo.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleRequestDTO roleRequestDTO);
    RoleResponseDTO toDTO(Role role);
    List<RoleResponseDTO> toDTOs(List<Role> roles);
}
