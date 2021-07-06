package com.sankalp.SpringJwtDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sankalp.SpringJwtDemo.helper.JwtUtil;
import com.sankalp.SpringJwtDemo.models.AuthenticationRequest;
import com.sankalp.SpringJwtDemo.models.AuthenticationResponse;
import com.sankalp.SpringJwtDemo.services.CustomUserDetailsService;

@RestController
@RequestMapping("/api")
public class Home{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Hello World!";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception{
		try	{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or password", e);
		}
		
		final UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
}
