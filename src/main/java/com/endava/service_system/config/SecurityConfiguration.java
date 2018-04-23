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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Order(2)
    @Configuration
    public static class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;
        private final LogoutSuccessHandler logoutSuccessHandler;
        private final DaoAuthenticationProvider daoAuthenticationProvider;

        public AdminSecurityConfiguration(@Qualifier("adminDetailsService") UserDetailsService userDetailsService,
                                          LogoutSuccessHandler logoutSuccessHandler,
                                          @Qualifier("adminDaoAuthProvider") DaoAuthenticationProvider daoAuthenticationProvider) {
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
                    .antMatchers("/admin", "/admin/login").permitAll()
                    .antMatchers("/admin/logout").authenticated()
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .and().formLogin().loginPage("/admin/login")
                    .defaultSuccessUrl("/admin/panel")
                    .usernameParameter("username").passwordParameter("password")
                    .and().csrf().disable();
            //TODO add csrf in all forms and enable crsf (for protection)!!
            http.logout().deleteCookies()
                    .logoutUrl("/admin/logout").invalidateHttpSession(true).clearAuthentication(true)
                    .logoutSuccessUrl("/").logoutSuccessHandler(logoutSuccessHandler);
        }
    }

}
