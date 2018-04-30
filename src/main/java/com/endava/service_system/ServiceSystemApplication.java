package com.endava.service_system;

import com.endava.service_system.intercepter.LogoutNotAcceptedStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@SpringBootApplication
public class ServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSystemApplication.class, args);
	}

	@Configuration
	static class MyConfiguration implements WebMvcConfigurer {

		private LogoutNotAcceptedStatus logoutNotAcceptedStatus;
		@Bean
		@Primary
		public ConversionServiceFactoryBean conversionFacilitator(Set<Converter> converters) {
			ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
			factory.setConverters(converters);
			return factory;
		}

		//TODO add all paths that requires you to be authenticated
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(logoutNotAcceptedStatus).addPathPatterns("/admin/**","/user/**");
		}

		@Autowired
		public void setLogoutNotAcceptedStatus(LogoutNotAcceptedStatus logoutNotAcceptedStatus) {
			this.logoutNotAcceptedStatus = logoutNotAcceptedStatus;
		}
	}


}
