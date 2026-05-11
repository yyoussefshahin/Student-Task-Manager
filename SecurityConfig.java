package com.project.TaskManger.conf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/landing", "/register", "/login", "/student/login", "/admin/login", "/student/register", "/admin/register", "/css/**", "/h2-console/**").permitAll()
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")

                        .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                            String formRole = request.getParameter("role");

                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            try {
                                if ("ADMIN".equalsIgnoreCase(formRole) && !isAdmin) {
                                    SecurityContextHolder.clearContext();
                                    request.getSession().invalidate();
                                    response.sendRedirect("/admin/login?error=role");
                                } else if ("STUDENT".equalsIgnoreCase(formRole) && isAdmin) {
                                    SecurityContextHolder.clearContext();
                                    request.getSession().invalidate();
                                    response.sendRedirect("/student/login?error=role");
                                } else {
                                    response.sendRedirect("/redirect");
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })

                        .failureHandler((HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
                            String role = request.getParameter("role");
                            try {
                                if ("ADMIN".equals(role)) {
                                    response.sendRedirect("/admin/login?error=true");
                                } else {
                                    response.sendRedirect("/student/login?error=true");
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/landing")
                        .permitAll()
                )
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .csrf(c -> c.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}