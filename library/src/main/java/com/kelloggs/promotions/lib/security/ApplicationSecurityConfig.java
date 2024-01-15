/*
@Author
Abhishek - M1050754
abhishek@mindtree.com
 */
package com.kelloggs.promotions.lib.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("applicationUser")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/users").permitAll()
                .antMatchers("/api/v1/snipp/ocrInfo", "/api/v2/snipp/ocrInfo").hasAnyRole("SNIPP", "ADMIN")
                .antMatchers("/api/v1/token/validate", "/api/v1/promotions/entry/{entryId}/update/score", 
                		"/api/v1/promotions/{promotionId}/entries").hasAnyRole("BLIPPAR", "PEEKNPOKE", "AEM", "ADMIN")
                .antMatchers("/api/v1/winners/entry/{entryId}/update/selection/result", 
                		"/api/v1/winners/entry/{promotionEntryId}/select").hasAnyRole("BLIPPAR", "AEM", "ADMIN")
                .antMatchers("/api/v1/promotions/{promotionId}/user/game/entry", "/api/v1/promotions/user/get", 
                		"/api/v1/promotions/user/update").hasAnyRole("JVM", "AEM", "ADMIN")
                .antMatchers("/**").hasAnyRole("AEM", "ADMIN","POPTOPIA")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
