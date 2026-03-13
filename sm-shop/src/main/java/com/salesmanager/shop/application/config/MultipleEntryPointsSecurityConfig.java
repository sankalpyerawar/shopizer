package com.salesmanager.shop.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.salesmanager.shop.admin.security.UserAuthenticationSuccessHandler;
import com.salesmanager.shop.admin.security.WebUserServices;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.security.AuthenticationTokenFilter;
import com.salesmanager.shop.store.security.ServicesAuthenticationSuccessHandler;
import com.salesmanager.shop.store.security.admin.JWTAdminAuthenticationProvider;
import com.salesmanager.shop.store.security.admin.JWTAdminServicesImpl;
import com.salesmanager.shop.store.security.customer.JWTCustomerAuthenticationProvider;
import com.salesmanager.shop.store.security.services.CredentialsService;
import com.salesmanager.shop.store.security.services.CredentialsServiceImpl;

/**
 * Main entry point for security - admin - customer - auth - private - services
 * Migrated to Spring Security 6 SecurityFilterChain pattern
 * 
 * TODO: Review and test all security configurations thoroughly
 * TODO: Verify authentication and authorization work correctly
 * TODO: Test all endpoints with proper credentials
 */
@Configuration
@EnableWebSecurity
public class MultipleEntryPointsSecurityConfig {

	private static final String API_VERSION = "/api/v*";

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilter() {
		return new AuthenticationTokenFilter();
	}
	
	@Bean
	public CredentialsService credentialsService() {
		return new CredentialsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
		return new UserAuthenticationSuccessHandler();
	}

	@Bean
	public ServicesAuthenticationSuccessHandler servicesAuthenticationSuccessHandler() {
		return new ServicesAuthenticationSuccessHandler();
	}

	@Bean
	public CustomerFacade customerFacade() {
		return new com.salesmanager.shop.store.controller.customer.facade.CustomerFacadeImpl();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers("/")
			.requestMatchers("/error")
			.requestMatchers("/resources/**")
			.requestMatchers("/static/**")
			.requestMatchers("/services/public/**");
	}

	/**
	 * Customer security configuration
	 */
	@Configuration
	@Order(1)
	public static class CustomerSecurityConfig {

		@Autowired
		private UserDetailsService customerDetailsService;

		@Bean
		public AuthenticationManager customerAuthenticationManager(AuthenticationConfiguration authConfig) throws Exception {
			return authConfig.getAuthenticationManager();
		}

		@Bean
		@Order(1)
		public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
			http
				.securityMatcher("/shop/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
					.requestMatchers("/shop/").permitAll()
					.requestMatchers("/shop/**").permitAll()
					.requestMatchers("/shop/customer/logon*").permitAll()
					.requestMatchers("/shop/customer/registration*").permitAll()
					.requestMatchers("/shop/customer/logout*").permitAll()
					.requestMatchers("/shop/customer/customLogon*").permitAll()
					.requestMatchers("/shop/customer/denied*").permitAll()
					.requestMatchers("/shop/customer/**").hasRole("AUTH_CUSTOMER")
					.anyRequest().authenticated()
				)
				.httpBasic(basic -> basic.authenticationEntryPoint(shopAuthenticationEntryPoint()))
				.logout(logout -> logout
					.logoutUrl("/shop/customer/logout")
					.logoutSuccessUrl("/shop/")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
				)
				.exceptionHandling(ex -> ex.accessDeniedPage("/shop/"));

			return http.build();
		}

		@Bean
		public AuthenticationEntryPoint shopAuthenticationEntryPoint() {
			BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
			entryPoint.setRealmName("shop-realm");
			return entryPoint;
		}
	}

	/**
	 * Services API security configuration
	 */
	@Configuration
	@Order(2)
	public static class ServicesApiSecurityConfig {

		@Autowired
		private JWTCustomerAuthenticationProvider jwtCustomerAuthenticationProvider;

		@Autowired
		private AuthenticationTokenFilter authenticationTokenFilter;

		@Bean
		@Order(2)
		public SecurityFilterChain servicesApiFilterChain(HttpSecurity http) throws Exception {
			http
				.securityMatcher("/services/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
					.requestMatchers("/services/public/**").permitAll()
					.requestMatchers("/services/private/**").hasRole("AUTH_CUSTOMER")
					.anyRequest().authenticated()
				)
				.authenticationProvider(jwtCustomerAuthenticationProvider)
				.addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class);

			return http.build();
		}
	}

	/**
	 * Admin security configuration
	 */
	@Configuration
	@Order(3)
	public static class AdminSecurityConfig {

		@Autowired
		private WebUserServices webUserServices;

		@Bean
		public AuthenticationManager adminAuthenticationManager(AuthenticationConfiguration authConfig) throws Exception {
			return authConfig.getAuthenticationManager();
		}

		@Bean
		@Order(3)
		public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
			http
				.securityMatcher("/admin/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
					.requestMatchers("/admin/").permitAll()
					.requestMatchers("/admin/logon*").permitAll()
					.requestMatchers("/admin/denied*").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated()
				)
				.formLogin(form -> form
					.loginPage("/admin/logon.html")
					.loginProcessingUrl("/admin/login")
					.successHandler(userAuthenticationSuccessHandler())
					.failureUrl("/admin/logon.html?login_error=true")
					.permitAll()
				)
				.logout(logout -> logout
					.logoutUrl("/admin/logout")
					.logoutSuccessUrl("/admin/")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
				)
				.exceptionHandling(ex -> ex.accessDeniedPage("/admin/denied.html"));

			return http.build();
		}

		@Bean
		public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
			return new UserAuthenticationSuccessHandler();
		}
	}

	/**
	 * User API security configuration
	 */
	@Configuration
	@Order(4)
	public static class UserApiSecurityConfig {

		@Autowired
		private JWTAdminAuthenticationProvider jwtAdminAuthenticationProvider;

		@Autowired
		private AuthenticationTokenFilter authenticationTokenFilter;

		@Bean
		@Order(4)
		public SecurityFilterChain userApiFilterChain(HttpSecurity http) throws Exception {
			http
				.securityMatcher(API_VERSION + "/user/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.POST, API_VERSION + "/user/login").permitAll()
					.requestMatchers(HttpMethod.POST, API_VERSION + "/user/password/**").permitAll()
					.requestMatchers(API_VERSION + "/user/**").hasRole("AUTH")
					.anyRequest().authenticated()
				)
				.authenticationProvider(jwtAdminAuthenticationProvider)
				.addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class);

			return http.build();
		}
	}

	/**
	 * Customer API security configuration
	 */
	@Configuration
	@Order(5)
	public static class CustomerApiSecurityConfig {

		@Autowired
		private JWTCustomerAuthenticationProvider jwtCustomerAuthenticationProvider;

		@Autowired
		private AuthenticationTokenFilter authenticationTokenFilter;

		@Bean
		@Order(5)
		public SecurityFilterChain customerApiFilterChain(HttpSecurity http) throws Exception {
			http
				.securityMatcher(API_VERSION + "/customer/**", API_VERSION + "/auth/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.POST, API_VERSION + "/customer/login").permitAll()
					.requestMatchers(HttpMethod.POST, API_VERSION + "/customer").permitAll()
					.requestMatchers(HttpMethod.POST, API_VERSION + "/customer/password/**").permitAll()
					.requestMatchers(HttpMethod.POST, API_VERSION + "/auth/**").permitAll()
					.requestMatchers(API_VERSION + "/customer/**").hasRole("AUTH_CUSTOMER")
					.requestMatchers(API_VERSION + "/auth/**").hasRole("AUTH_CUSTOMER")
					.anyRequest().authenticated()
				)
				.authenticationProvider(jwtCustomerAuthenticationProvider)
				.addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class);

			return http.build();
		}
	}
}
