package com.endava.service_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Configuration
public class AppConfiguration{

	@Value("${bankApi}")
	private String bankApi;
	@Value("${siteUrl}")
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

	protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
	{
		return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
	}

	@Bean
	public SpringTemplateEngine templateEngine(AbstractTemplateResolver abstractTemplateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(abstractTemplateResolver);
		templateEngine.addDialect(new SpringSecurityDialect());
		return templateEngine;
	}

	@Bean
	public AuthenticationFailureHandler init(){
		return new AuthHandl();
	}

	private class AuthHandl extends SimpleUrlAuthenticationFailureHandler{
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
			String path="/";
			final FlashMap flashMap = new FlashMap();
			// Don't send the AuthenticationException object itself because it has no default constructor and cannot be re-instantiated.
			flashMap.put("error", exception.getMessage());
			final FlashMapManager flashMapManager = new SessionFlashMapManager();
			flashMapManager.saveOutputFlashMap(flashMap, request, response);
			if(getPreviousPageByRequest(request).isPresent()){
				path="/login";
			}

			request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION",exception);
			response.sendRedirect(path);
		}
	}
}
