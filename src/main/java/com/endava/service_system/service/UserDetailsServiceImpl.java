package com.endava.service_system.service;

import com.endava.service_system.model.User;
import com.endava.service_system.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.getByUsername(username);
        userOptional.orElseThrow(()->new UsernameNotFoundException("User with username " + username + " not found"));
        User user = userOptional.get();
        Set<GrantedAuthority> grantedAuthorities = Stream.of(new SimpleGrantedAuthority("ROLE_USER"))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
