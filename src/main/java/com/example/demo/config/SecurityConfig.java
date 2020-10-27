package com.example.demo.config;

import com.example.demo.manager.Manager;
import com.example.demo.manager.ManagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ManagerService managerService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.managerService).passwordEncoder(Manager.PASSWORD_ENCODER);
    }

    // @formatter:off
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
                .antMatchers("/static/**", "/*.js*", "/*.png", "/index.html", "robots.txt", "/*.ico").permitAll()
                .antMatchers(HttpMethod.GET, "/api/person/").permitAll()
                .antMatchers("/api/secret/**").hasRole("MANAGER")
                .anyRequest().authenticated()
				.and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                    .rememberMe()
				.and()
            .logout()
                .logoutSuccessUrl("/")
            .and()
                .csrf().disable()
                .httpBasic()
            ;
    }
    // @formatter:on
}
