package com.synergisticit.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.synergisticit.util.AuditorAwareImpl;

@Configuration
public class AppConfig {
	
		@Bean
		public AuditorAware<String> auditorAware() {
			return new AuditorAwareImpl();
		}

		@Bean
		InternalResourceViewResolver viewResolver() {
			InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
			viewResolver.setPrefix("WEB-INF/jsp/");
			viewResolver.setSuffix(".jsp");
			viewResolver.setViewClass(JstlView.class);
			return viewResolver;
		}
		
		@Bean
		public MessageSource messageSource() {
			ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
			messageSource.addBasenames("WEB-INF/message/messages");
			return messageSource;
		}
		
		@Bean
		public BCryptPasswordEncoder bCryptPasswordEncoder() {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			return encoder;
		}
}
