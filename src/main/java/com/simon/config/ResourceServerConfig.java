package com.simon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by simon on 2017/2/25.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //antMathcers先定义的优先级和高
        http
                .authorizeRequests()
                .antMatchers("/swagger-ui.html","/v2/api-docs").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/events", "POST")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/events/{id}", "PATCH")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/api/events/getByPublisher", "GET")).hasRole("ADMIN")
                .antMatchers("/api/**").permitAll()
                .antMatchers("/**").permitAll()
                //.antMatchers("/**").authenticated() will
                // cause the url "http://localhost:8080/swagger-ui.html" be blank html,
                //just remaining the head.
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
