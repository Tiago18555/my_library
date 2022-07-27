package com.TiagoSoftware.MyLibrary.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BcryptConfiguration {

    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder(5);
    }
}
