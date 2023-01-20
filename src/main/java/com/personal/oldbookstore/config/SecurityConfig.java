package com.personal.oldbookstore.config;

import com.personal.oldbookstore.config.auth.FormLoginFailureHandler;
import com.personal.oldbookstore.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final FormLoginFailureHandler formLoginFailureHandler;

    private final String[] whitelist = {
            "/resources/**", "/css/**", "/js/**", "/img/**",
            "/oauth2", "/api/**",
            "/",
            "/login", "/join", "/joinOk", "/findPass" +
            "word", "users/findPasswordEmailSend",
            "/items/list", "/items/list/**", "/item/{itemId}"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                    .antMatchers(whitelist).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/loginProcessing")
                    .defaultSuccessUrl("/")
                    .failureHandler(formLoginFailureHandler)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService);

        return http.build();
    }


}
