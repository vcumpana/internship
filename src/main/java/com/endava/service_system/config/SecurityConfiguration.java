package com.endava.service_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    public SecurityConfiguration(@Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                 LogoutSuccessHandler logoutSuccessHandler,
                                 @Qualifier("userDaoAuthProvider") DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.userDetailsService = userDetailsService;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //TODO $RUSTAM change register name to registration;
                .antMatchers("/", "/login", "/index","/user/register","/company/registration").permitAll()
                .antMatchers("/logout").authenticated()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .antMatchers("/company/**").access("hasRole('ROLE_COMPANY')")
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/success")
                .usernameParameter("username").passwordParameter("password");
        http.csrf().disable();
        //TODO add csrf in all forms and enable crsf (for protection)!!
        http.logout().deleteCookies()
                .logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true)
                .logoutSuccessUrl("/").logoutSuccessHandler(logoutSuccessHandler);
    }


}
