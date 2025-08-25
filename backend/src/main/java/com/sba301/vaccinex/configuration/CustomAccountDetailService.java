package com.sba301.vaccinex.configuration;

import com.sba301.vaccinex.pojo.User;
import com.sba301.vaccinex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getAccountByEmail(username);
        if(user != null) {
            return CustomAccountDetail.mapAccountToAccountDetail(user);
        }
        return null;
    }
}
