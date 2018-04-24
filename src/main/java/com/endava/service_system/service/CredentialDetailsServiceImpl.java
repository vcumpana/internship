package com.endava.service_system.service;

import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Credential;
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
public class CredentialDetailsServiceImpl implements UserDetailsService{

    private final CredentialService credentialService;

    public CredentialDetailsServiceImpl(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Credential> credentialOptional = credentialService.getByUsername(username);
        credentialOptional.orElseThrow(()->new UsernameNotFoundException("User with username " + username + " not found"));
        Credential user = credentialOptional.get();
        if(user.getStatus()!= UserStatus.ACCEPTED){
            throw new UsernameNotFoundException("User "+username+" access denied or is waiting");
        }
        Set<GrantedAuthority> grantedAuthorities = Stream.of(new SimpleGrantedAuthority(user.getRole().name()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
