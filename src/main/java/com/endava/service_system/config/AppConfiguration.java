package com.endava.service_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfiguration{

	@Value("${bankApi}")
	private String bankApi;
	@Value("${siteUrl")
	private String siteUrl;
	@Bean 
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new SimpleUrlLogoutSuccessHandler();
	}

	@Bean
	@Qualifier("userDaoAuthProvider")
	public DaoAuthenticationProvider authenticationProviderForUser(@Autowired PasswordEncoder passwordEncoder, @Qualifier("userDetailsService") UserDetailsService userDetailsService) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setHideUserNotFoundExceptions(false) ;
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	@Bean
	@Qualifier("bankApi")
	public String getBankApi(){
		return bankApi;
	}
	@Bean
	@Qualifier("siteUrl")
	public String getSiteUrl(){
		return siteUrl;
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	public KeyPairGenerator getKeyPairGenerator(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
		keyGen.initialize(4096);
		return keyGen;
	}

	@Bean
	public KeyFactory getKeyFactory(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchAlgorithmException {
		return KeyFactory.getInstance(algorithm);
	}

	@Bean
	@Qualifier("encrypter")
	public Cipher getEncrypter(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
		return Cipher.getInstance(algorithm);
	}

	@Bean
	@Qualifier("decrypter")
	public Cipher getDecrypter(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
		return Cipher.getInstance(algorithm);
	}

	@Bean
	@Qualifier("encryptionAlgorithm")
	public String algorithm(){
		return "RSA";
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}
}
