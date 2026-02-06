package org.example.ecommerce.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String jwt;
		final String username;

		// Extract JWT from Cookie
		jwt = getJwtFromCookie(request);

		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		// if extractUsername does not throw any exception -> means the token is valid
		// and not expired;
		username = jwtService.extractUsername(jwt);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromCookie(HttpServletRequest request) {
		if (request.getCookies() == null) {
			return null;
		}
//		Cookie[] cookies=request.getCookies();
//		for(Cookie cookie:cookies) {
//			if(cookie.getName().equals("jwt")) {
//				return cookie.getValue();
//			}
//		}
//		return null;
		return Arrays.stream(request.getCookies()).filter(cookie -> "jwt".equals(cookie.getName()))
				.map(Cookie::getValue).findAny().orElse(null);
	}
}
