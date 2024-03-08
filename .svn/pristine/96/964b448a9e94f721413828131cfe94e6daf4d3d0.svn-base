package com.kelloggs.promotions.lib.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing
@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        final Integer STRENGTH = 10;
        return new BCryptPasswordEncoder(STRENGTH);
    }
}
