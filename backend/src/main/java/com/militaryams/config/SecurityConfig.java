package com.militaryams.backend.config;

import com.militaryams.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .userDetailsService(customUserDetailsService)
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/login").permitAll()

                .requestMatchers("/api/users/**")
                .hasRole("ADMIN")

                .requestMatchers("/api/assignments/**")
                .hasAnyRole("ADMIN", "BASE_COMMANDER")

                .requestMatchers("/api/expenditures/**")
                .hasAnyRole("ADMIN", "BASE_COMMANDER")

                .requestMatchers("/api/purchases/**")
                .hasAnyRole(
                    "ADMIN",
                    "BASE_COMMANDER",
                    "LOGISTICS_OFFICER"
                )

                .requestMatchers("/api/transfers/**")
                .hasAnyRole(
                    "ADMIN",
                    "BASE_COMMANDER",
                    "LOGISTICS_OFFICER"
                )

                .requestMatchers("/api/dashboard/**")
                .authenticated()

                .requestMatchers("/api/audit-logs/**")
                .authenticated()

                .anyRequest()
                .authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}