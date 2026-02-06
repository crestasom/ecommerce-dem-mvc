package org.example.ecommerce.config;

import java.io.IOException;

import org.example.ecommerce.service.JwtAuthenticationFilter;
import org.example.ecommerce.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**", "/error", "/.well-known/**")
						.permitAll().requestMatchers("/product/", "/product/edit/**", "/product/delete/**")
						.hasRole("ADMIN").anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

				.formLogin(form -> form.loginPage("/auth/login").permitAll()

						.successHandler(successHandler()))

				.logout(logout -> logout.logoutUrl("/auth/logout").deleteCookies("jwt").logoutSuccessUrl("/auth/login"))
		// .exceptionHandling(ex ->
		// ex.authenticationEntryPoint(customAuthenticationEntryPoint()))
		;
		return http.build();
	}

	// uncomment this if you only need to add jwt token but still use form login
	AuthenticationSuccessHandler successHandler() {

		return new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {

				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				String jwt = jwtService.generateToken(userDetails);

				// Create Cookie
				Cookie jwtCookie = new Cookie("jwt", jwt);
				jwtCookie.setHttpOnly(true);
				jwtCookie.setPath("/");
				jwtCookie.setMaxAge(86400); // 1 day
				// jwtCookie.setSecure(true); // Uncomment if using HTTPS

				response.addCookie(jwtCookie);
				response.sendRedirect("/");
//					return "redirect:/";

			}
		};
	}
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	/*
	 * METHOD 1: Default Starter Security By default, Spring Boot Security provides
	 * a default user with a generated password (visible in the console) if no
	 * UserDetailsService or AuthenticationProvider is defined.
	 */
//
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(
//				auth -> auth.requestMatchers("/auth/login", "/css/**", "/js/**", "/error", "/.well-known/**")
//						.permitAll().anyRequest().authenticated())
//				.formLogin(form -> form.loginPage("/auth/login").permitAll()).logout(logout -> logout
//						.logoutUrl("/auth/logout").permitAll().deleteCookies("jwt").logoutSuccessUrl("/auth/login"));
//
//		return http.build();
//	}
//
//	@Bean
//	UserDetailsService userDetailsService(PasswordEncoder encoder) {
//		UserDetails user = User.withUsername("admin").password(encoder.encode("password")).roles("ADMIN").build();
//
//		return new InMemoryUserDetailsManager(user);
//	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	/*
	 * METHOD 2: Configuring Password Encoder Instead of using {noop} or default
	 * encoders, we can define a PasswordEncoder bean. BCrypt is the recommended
	 * standard for hashing passwords.
	 */
	// @Bean
	// public PasswordEncoder passwordEncoder() {
	// return new BCryptPasswordEncoder();
	// }

	/*
	 * METHOD 3: Loading User Details from Database Using a custom
	 * UserDetailsService allows us to load user information from our own database
	 * (e.g., via JPA UserRepository).
	 */
	// @Bean
	// public UserDetailsService
	// customUserDetailsService(org.example.repository.UserRepository
	// userRepository) {
	// return new org.example.security.CustomUserDetailsService(userRepository);
	// }

	/*
	 * METHOD 4: Custom Authentication Checking Mechanism By defining a custom
	 * AuthenticationProvider, we gain full control over the authentication process,
	 * including password matching and user status checks. Our implementation
	 * (CustomAuthenticationProvider) checks user.isActive().
	 */
	// @Bean
	// public AuthenticationProvider
	// customAuthenticationProvider(org.example.repository.UserRepository
	// userRepository) {
	// return new org.example.security.CustomAuthenticationProvider(userRepository);
	// }

	// For this demo to work without manual uncommenting, let's provide a simple
	// NoOp encoder if none is active
//        @Bean
//        public PasswordEncoder fallbackPasswordEncoder() {
//                return NoOpPasswordEncoder.getInstance();
//        }


//	
//
//	
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	AuthenticationEntryPoint customAuthenticationEntryPoint() {
//		return new AuthenticationEntryPoint() {
//			@Override
//			public void commence(HttpServletRequest request, HttpServletResponse response,
//					AuthenticationException authException) throws IOException {
//
//				response.sendRedirect("/auth/login");
//			}
//		};
//	}
}
