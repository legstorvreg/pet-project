package ru.streltsova.pet_project.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE) // для того, чтобы скрыть конфиг приложения
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public TestAuthFilter testAuthFilter() {
        return new TestAuthFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(testAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }
}
