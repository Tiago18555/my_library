package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.Entity.Auth;
import com.TiagoSoftware.MyLibrary.Repositories.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final AuthRepository dbset;

    public UserService(AuthRepository dbset) {
        this.dbset = dbset;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles;
        Optional<Auth> user = dbset.findByUsername(username);

        if (user.isPresent()) {
            roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(user.get().getUsername(), user.get().getPassword(), roles);
        }
        throw new UsernameNotFoundException(username + " não encontrado");
    }
}
