package com.sba301.vaccinex.mapper;

import com.sba301.vaccinex.dto.AccountDTO;
import com.sba301.vaccinex.dto.response.CustomerInfoResponse;
import com.sba301.vaccinex.dto.response.DoctorResponseDTO;
import com.sba301.vaccinex.pojo.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "nonLocked", target = "nonLocked")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "id", target = "id")
    AccountDTO accountToAccountDTO(User user);
    DoctorResponseDTO toDoctorDTO(User user);
    List<DoctorResponseDTO> toDoctorDTOs(List<User> doctor);

    CustomerInfoResponse toCustomerInfoResponse(User user);
}
