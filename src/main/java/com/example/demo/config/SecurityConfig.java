package com.example.demo.config;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.manager.Manager;
import com.example.demo.manager.ManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ManagerService managerService;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public RequestBodyReaderAuthenticationFilter authenticationFilter() throws Exception {
        RequestBodyReaderAuthenticationFilter authenticationFilter = new RequestBodyReaderAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(
                (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK));
        authenticationFilter.setAuthenticationFailureHandler(
                (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.managerService).passwordEncoder(Manager.PASSWORD_ENCODER);
    }

    // @formatter:off
    @Override
	protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .authorizeRequests()
                .antMatchers(
                    HttpMethod.GET,
                    "/index*", "/static/**", "/*.js", "/*.json", "/*.ico", "robots.txt"
                ).permitAll()
                .antMatchers("/api/person/**").permitAll()
                .antMatchers("/login", "/logout").permitAll()
                .antMatchers("/api/v1/**").permitAll()
                .antMatchers("/api/account/**").permitAll()
                .antMatchers("/api/secret/**").hasRole("MANAGER")
                .anyRequest().denyAll()
            .and()
                .addFilterBefore(
                    authenticationFilter(),
                    UsernamePasswordAuthenticationFilter.class)
                .logout()
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler((request, response, authentication) -> 
                        response.setStatus(HttpServletResponse.SC_OK)
                    )
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
            ;
    }
    // @formatter:on

}
