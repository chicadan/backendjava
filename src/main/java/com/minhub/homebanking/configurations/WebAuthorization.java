package com.minhub.homebanking.configurations;

import com.minhub.homebanking.models.RoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;

@EnableWebSecurity

@Configuration
public class WebAuthorization  {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {

        http.authorizeRequests()
      /*         .antMatchers("/web/index.html", "/web/js/**","/web/css/**","/web/img/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login","/api/logout","/api/clients").permitAll()
                .antMatchers("/h2-console/**","/rest/**", "/api/clients").hasAuthority("ADMIN")
                .antMatchers("/api/**","/api/clients/{id}","/loans").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current", "/web/**","/api/clients/**","/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/clients/current/accounts/**","/clients/current/cards","/transactions","/loans").hasAuthority("CLIENT")
                .anyRequest().denyAll();*/


                .antMatchers("/web/index.html","/web/js/**","/web/css/**","/web/img/**").permitAll() // PUBLIC ALL FE
                .antMatchers(HttpMethod.POST,"/api/login", "/api/logout").permitAll() //PUBLIC ACCESS TO SIGN IN
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()//PUBLIC ACCESS TO CREATE
                .antMatchers("/api/clients/current","/web/**","/api/clients/current/accounts","/api/loans").hasAuthority("CLIENT") // ONLY CLIENT
                .antMatchers(HttpMethod.POST,"/api/clients/current/**", "/api/transactions","/api/loans","/api/**").hasAuthority("CLIENT")//CLIENT ACCESS TO CREATE
                .antMatchers("/rest/**","/h2-console/**").hasAuthority("ADMIN") // ONLY ADMIN
                .anyRequest().denyAll();//REST DENY ALL




        http.formLogin()

                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");




        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }


}