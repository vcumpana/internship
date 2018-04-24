package com.endava.service_system.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.endava.service_system.model.Admin;
import com.endava.service_system.service.AdminService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminDetailsService")
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminService adminService;
    
    public AdminDetailsServiceImpl(AdminService service) {
    	adminService =service;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> adminOptional= adminService.getByUsername(username);
        adminOptional.orElseThrow(()->new UsernameNotFoundException("Admin with username "+username + " not found"));
        Admin admin = adminOptional.get();
        Set<GrantedAuthority> grantedAuthorities = Stream.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(), admin.getPassword(), grantedAuthorities);
    }

}
