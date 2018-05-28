package com.endava.service_system;

import com.endava.service_system.intercepter.LogoutNotAcceptedStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@SpringBootApplication
@EnableScheduling
public class ServiceSystemApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSystemApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return super.configure(builder);
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
			registry.addInterceptor(logoutNotAcceptedStatus)
					.addPathPatterns("/user/**","/company/**","/admin/**",
							"/category","/invoces","/bank/**","/notification/**",
							"/image/*","/user/selfUpdatePassword","/services/*",
							"/email/test",
							"/newContract","/invoice/payInvoice","/services","/*/services","/services/getPDF",
							"contract/**","invoice/*/*","/service/**")
					.excludePathPatterns("/", "/login", "/index", "/services","/contact","/user/registration","/company/registration","/forgotPassword","/resetPassword/*");
		}

		@Autowired
		public void setLogoutNotAcceptedStatus(LogoutNotAcceptedStatus logoutNotAcceptedStatus) {
			this.logoutNotAcceptedStatus = logoutNotAcceptedStatus;
		}
	}


}
