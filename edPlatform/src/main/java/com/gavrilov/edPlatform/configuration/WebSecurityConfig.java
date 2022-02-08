package com.gavrilov.edPlatform.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //configure login
        http
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/courses/all", true)
                .permitAll();

        //configure logout
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and();

        //configure url authorization
        http
                .authorizeRequests()
                .antMatchers("/register", "/courses/all", "/css/*", "/successfulRegistration", "/courses/search/**", "/courses/view/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and();

        //other settings
        http
                .csrf().disable();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
