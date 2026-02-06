package org.example.ecommerce.controller;

import org.example.ecommerce.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final PasswordEncoder encoder;

	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("error", "Invalid username or password");
		}
		return "auth/login";
	}

//	@PostMapping("/login")
//	public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
//		try {
//
//			// load user by username
//			// comare the password from request with password of load user
//			// make sure to use password encoder
//			// password.equals(user.getPassword())
//			//
//
//			Authentication authentication = authenticationManager
//					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			String jwt = jwtService.generateToken(userDetails);
//
//			// Create Cookie
//			Cookie jwtCookie = new Cookie("jwt", jwt);
//			jwtCookie.setHttpOnly(true);
//			jwtCookie.setPath("/");
//			jwtCookie.setMaxAge(86400); // 1 day
//			// jwtCookie.setSecure(true); // Uncomment if using HTTPS
//
//			response.addCookie(jwtCookie);
//
//			return "redirect:/";
//		} catch (Exception e) {
//			return "redirect:/auth/login?error=true";
//		}
//	}
}
