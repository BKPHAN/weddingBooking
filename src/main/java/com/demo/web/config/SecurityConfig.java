package com.demo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Bỏ qua CSRF cho các API endpoint nếu sử dụng REST API
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                // Cấu hình các yêu cầu bảo mật
                .authorizeRequests()
                // Cho phép truy cập không cần đăng nhập đối với các trang sau
                .requestMatchers("/", "/login", "api/register", "api/login", "/wedding", "/about", "/booking", "/services", "/contact", "/api/feedbacks","/api/booking", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()  // Cho phép tài nguyên tĩnh và trang login
                // Các yêu cầu còn lại phải đăng nhập
                .anyRequest().authenticated()
                .and()
                // Cấu hình trang đăng nhập tùy chỉnh
                .formLogin(formLogin -> formLogin
                                .loginPage("/login")  // Địa chỉ trang đăng nhập
                                .permitAll() // Cho phép truy cập trang đăng nhập mà không cần đăng nhập
//                        .defaultSuccessUrl("/wedding", true)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Cấu hình user mặc định lưu tạm vào bộ nhớ thay vì database-
        authenticationManagerBuilder
                .inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user@example.com")
                        .password(passwordEncoder.encode("password"))
                        .roles("USER")
                        .build());

        // Trả về AuthenticationManager sau khi cấu hình
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
