package com.proofme.manspitub.ProofMeProject.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proofme.manspitub.ProofMeProject.dto.UserDtoConverter;
import com.proofme.manspitub.ProofMeProject.security.jwt.JwtProvider;
import com.proofme.manspitub.ProofMeProject.service.EmailService;
import com.proofme.manspitub.ProofMeProject.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserDtoConverter userDtoConverter;
	
	
	
}
