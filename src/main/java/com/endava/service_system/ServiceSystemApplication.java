package com.endava.service_system;

import com.endava.service_system.utils.CompanyRegistrationDTOToCompanyConverter;
import com.endava.service_system.utils.ContractDtoFromUserConverter;
import com.endava.service_system.utils.ServiceToUserDTOConverter;
import com.endava.service_system.utils.UserDtoToUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSystemApplication.class, args);
	}

	@Configuration
	static class MyConfiguration implements WebMvcConfigurer {
//		@Override
//		public void addFormatters(FormatterRegistry registry){
//			registry.addConverter(new UserDtoToUserConverter());
//            registry.addConverter(new CompanyRegistrationDTOToCompanyConverter());
//            registry.addConverter(new ServiceToUserDTOConverter());
//            registry.addConverter(new ContractDtoFromUserConverter());
//		}
		@Bean
		@Primary
		//@Autowired
		public ConversionServiceFactoryBean conversionFacilitator(Set<Converter> converters) {
			ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
			factory.setConverters(converters);
			return factory;
		}
	}


}
