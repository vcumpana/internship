package com.endava.service_system;

import com.endava.service_system.utils.UserDtoToUserConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSystemApplication.class, args);
	}

	@Configuration
	static class MyConfiguration implements WebMvcConfigurer{
		@Override
		public void addFormatters(FormatterRegistry registry){
			registry.addConverter(new UserDtoToUserConverter());
		}
	}
}
