package com.TiagoSoftware.MyLibrary.Configuration;

import com.TiagoSoftware.MyLibrary.Models.Entity.Auth;
import com.TiagoSoftware.MyLibrary.Repositories.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthRepository userRepository;

    public UserDetailsServiceImpl(AuthRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Auth> auth = userRepository
                .findByUsername(username);
        return auth.get();
    }
}
