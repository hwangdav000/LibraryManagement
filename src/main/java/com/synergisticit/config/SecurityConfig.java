package com.synergisticit.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	@Autowired DataSource dataSource;
	@Autowired BCryptPasswordEncoder bCrypt;
	@Autowired UserDetailsService userDetailsService;
	
	@Bean
	DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(bCrypt);
		return authProvider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
		.requestMatchers(AntPathRequestMatcher.antMatcher("/home")).permitAll()
		.requestMatchers(AntPathRequestMatcher.antMatcher("/roleForm")).hasAnyAuthority("ADMIN")
		.requestMatchers(AntPathRequestMatcher.antMatcher("/userForm")).hasAnyAuthority("ADMIN")
		.requestMatchers(AntPathRequestMatcher.antMatcher("/librarianForm")).hasAnyAuthority("ADMIN")
		.requestMatchers(AntPathRequestMatcher.antMatcher("/memberForm")).permitAll()
		.requestMatchers(AntPathRequestMatcher.antMatcher("/publisherForm")).hasAnyAuthority("ADMIN")
		.requestMatchers(AntPathRequestMatcher.antMatcher("/transactionForm")).hasAnyAuthority("ADMIN", "USER")
		.requestMatchers(AntPathRequestMatcher.antMatcher("/transactionHistory")).hasAnyAuthority("ADMIN", "USER")
		.and()
		.httpBasic(Customizer.withDefaults())
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/home", true)
			.permitAll()
		.and()
		.logout()
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID");
		
		http.userDetailsService(userDetailsService);
		return http.build();
	}
}
