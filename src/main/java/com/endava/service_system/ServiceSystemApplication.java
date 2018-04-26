package com.endava.service_system;

import com.endava.service_system.utils.CompanyRegistrationDTOToCompanyConverter;
import com.endava.service_system.utils.ServiceToUserDTOConverter;
import com.endava.service_system.utils.UserDtoToUserConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSystemApplication.class, args);
	}

	@Configuration
	static class MyConfiguration implements WebMvcConfigurer {
		@Override
		public void addFormatters(FormatterRegistry registry){
			registry.addConverter(new UserDtoToUserConverter());
            registry.addConverter(new CompanyRegistrationDTOToCompanyConverter());
            registry.addConverter(new ServiceToUserDTOConverter());
		}
	}


}
