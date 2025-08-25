package com.sba301.vaccinex.mapper;
import com.sba301.vaccinex.dto.request.ChildrenRequestDTO;
import com.sba301.vaccinex.dto.response.ChildrenResponseDTO;
import com.sba301.vaccinex.pojo.Child;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChildrenMapper {
    ChildrenMapper INSTANCE = Mappers.getMapper(ChildrenMapper.class);

    Child toEntity(ChildrenRequestDTO childRequestDTO);
    ChildrenResponseDTO toDTO(Child child);
    List<ChildrenResponseDTO> toDTOs(List<Child> child);
}
