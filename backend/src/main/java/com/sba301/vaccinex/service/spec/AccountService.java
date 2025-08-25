package com.sba301.vaccinex.service.spec;

import com.sba301.vaccinex.dto.AccountDTO;
import com.sba301.vaccinex.dto.request.AccountRegisterRequest;
import com.sba301.vaccinex.dto.response.DoctorResponseDTO;
import com.sba301.vaccinex.dto.response.TokenResponse;
import com.sba301.vaccinex.pojo.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AccountService extends BaseService<User, Integer> {

    AccountDTO registerAccount(AccountRegisterRequest accountRegisterRequest);

    TokenResponse refreshToken(String refreshToken);

    TokenResponse login(String email, String password);

    boolean logout(HttpServletRequest request);

    User getUserById(Integer id);

    List<DoctorResponseDTO> findAllDoctors();

    DoctorResponseDTO findDoctorById(Integer doctorId);
}
