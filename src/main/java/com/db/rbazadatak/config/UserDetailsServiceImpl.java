package com.db.rbazadatak.config;

import com.db.rbazadatak.model.ApiUser;
import com.db.rbazadatak.repository.ApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@EnableWebSecurity
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ApiUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApiUser> apiUser = apiUserRepository.findApiUserByUsername(username);
        if (apiUser.isPresent()){
            return User
                    .withUsername(apiUser.get().getUsername())
                    .password(passwordEncoder().encode(apiUser.get().getPassword()))
                    .roles(apiUser.get().getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/person/**").hasRole("USER")
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
