package com.example.Todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/todo/{id}").permitAll()
//                        .requestMatchers("/api/todo").authenticated())
        .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
