package com.endava.service_system.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {
    public static void main(String[] args){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.matches("1qa2ws3ed!QA","$2a$10$3Z3GU9aUdaQ1bfaj/e.bbuVpiu5f4Lq7ffv/oF0rIRoaQvG0Y..Xu"));
    }
}
