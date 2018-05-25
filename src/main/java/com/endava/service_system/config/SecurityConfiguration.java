package com.endava.service_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public SecurityConfiguration(@Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                 LogoutSuccessHandler logoutSuccessHandler,
                                 @Qualifier("userDaoAuthProvider") DaoAuthenticationProvider daoAuthenticationProvider, AuthenticationFailureHandler authenticationFailureHandler) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.userDetailsService = userDetailsService;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/", "/login", "/index", "/services","/contact").permitAll()
                .antMatchers("/user/registration","/company/registration","/forgotPassword","/resetPassword/*").not().authenticated()
                .antMatchers("/logout","/category").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_COMPANY')")
                .antMatchers("/invoces","/bank/**","/notification/**","/image/*","/user/selfUpdatePassword","/services/*").access("hasRole('ROLE_USER') or hasRole('ROLE_COMPANY')")
                .antMatchers("/admin/**","/email/test").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**","/newContract","/invoice/payInvoice","/services","/*/services","/services/getPDF","/services/*").access("hasRole('ROLE_USER')")
                .antMatchers("/company/**","/contract/**","/invoice/*/*","/service/**").access("hasRole('ROLE_COMPANY')")
                .and().formLogin().loginPage("/login")
                .failureHandler(authenticationFailureHandler)
                .defaultSuccessUrl("/success")
                .usernameParameter("username").passwordParameter("password")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/success")
        ;
        http.csrf().disable();
        //TODO add csrf in all forms and enable crsf (for protection)!!
        http.logout().deleteCookies()
                .logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true)
                .logoutSuccessUrl("/").logoutSuccessHandler(logoutSuccessHandler);
    }

}
