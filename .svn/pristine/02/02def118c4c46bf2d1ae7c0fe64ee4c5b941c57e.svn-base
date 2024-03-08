package com.kelloggs.promotions.lib.security;

import com.kelloggs.promotions.lib.exception.ApiException;
import com.kelloggs.promotions.lib.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.kelloggs.promotions.lib.constants.ErrorCodes.NOT_FOUND;

@Service("applicationUser")
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepo
                .findByUserName(userName)
                .map(ApplicationUserDetails::new)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    NOT_FOUND.getCode(),
                                                    String.format("User %s not found", userName)));
    }
}
