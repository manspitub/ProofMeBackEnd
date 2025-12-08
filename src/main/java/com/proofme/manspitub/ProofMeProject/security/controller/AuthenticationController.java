package com.proofme.manspitub.ProofMeProject.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proofme.manspitub.ProofMeProject.dto.ApiResponse;
import com.proofme.manspitub.ProofMeProject.dto.UserDtoConverter;
import com.proofme.manspitub.ProofMeProject.enums.Role;
import com.proofme.manspitub.ProofMeProject.exception.EmailNotConfirmedException;
import com.proofme.manspitub.ProofMeProject.model.User;
import com.proofme.manspitub.ProofMeProject.security.dto.CreateUserDto;
import com.proofme.manspitub.ProofMeProject.security.dto.LoginDtoUser;
import com.proofme.manspitub.ProofMeProject.security.jwt.JwtProvider;
import com.proofme.manspitub.ProofMeProject.security.jwt.JwtUserResponse;
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

	// Para loguearte debes tener el atributo confirmed a true
	// LOGIN
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<JwtUserResponse>> loginUser(@RequestBody LoginDtoUser loginDtoUser) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDtoUser.getEmail(), loginDtoUser.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();

		if (!user.getEmailConfirmed()) {
			throw new EmailNotConfirmedException("Aún no has confirmado tu Email");
		}

		String jwt = jwtProvider.generateToken(authentication);
		JwtUserResponse response = convertUserClientToJwtUserResponse(user, jwt);

		return ResponseEntity.ok(new ApiResponse<>("Success", "Login correcto", response));
	}

	private JwtUserResponse convertUserClientToJwtUserResponse(User user, String jwt) {
		JwtUserResponse response = new JwtUserResponse(user.getName(), // name
				user.getEmail(), // email
				user.getRole().toString(), // role
				user.getSurname(), // surname
				user.getEmailConfirmed().booleanValue(), jwt // token
		);
		return response;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody CreateUserDto newUser) throws Exception {
		try {

			User userCreated = userService.addUser(newUser);
			boolean isCreatedByAdmin = userCreated.getRole().equals(Role.SUPPORTER);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtProvider.generateToken(authentication);

			emailService.sendConfirmationEmail(userCreated.getEmail(), userCreated.getName(), userCreated.getSurname(),
					jwt, isCreatedByAdmin, newUser.getPassword());
			return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.convertUserDto(userCreated));
		} catch (Exception e) {
			throw e;
		}
	}

	// CONFIRMACIÓN DE CUENTA
	@GetMapping("/confirm")
	public ResponseEntity<ApiResponse<?>> confirmAccount(@RequestParam("token") String token) {
		try {
			UsernamePasswordAuthenticationToken authentication = jwtProvider.decodeToken(token);

			userService.confirmUserAccount(authentication);

			return ResponseEntity.ok(new ApiResponse<>("success", "Cuenta activada con éxito", null));

		} catch (Exception e) {
			throw e;
		}
	}

}
