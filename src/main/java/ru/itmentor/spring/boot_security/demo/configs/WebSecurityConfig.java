package ru.itmentor.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itmentor.spring.boot_security.demo.configs.jwt.AuthEntryPointJwt;
import ru.itmentor.spring.boot_security.demo.configs.jwt.AuthTokenFilter;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImp;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserServiceImp userDetailsServiceImp;

    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler,
                             UserServiceImp userDetailsService,
                             AuthEntryPointJwt unauthorizedHandler) {
        this.successUserHandler = successUserHandler;
        this.userDetailsServiceImp = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/***").permitAll()
                .antMatchers("/test/***").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//        http.csrf().disable() //отключае защиту
//                .authorizeRequests()
//                .antMatchers("/auth/admin").hasRole("ADMIN")
//                .antMatchers("/users").hasRole("ADMIN")
//                .antMatchers("/", "/index", "/auth/registration", "/auth/login", "/error").permitAll()
//                .anyRequest().hasAnyRole("USER", "ADMIN")
//                .and()
//                .formLogin()
//                .defaultSuccessUrl("/", true)
//                .loginProcessingUrl("/process_login")
//                .failureUrl("/auth/login?error")
//                .successHandler(successUserHandler)
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/auth/login")
//                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImp)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}