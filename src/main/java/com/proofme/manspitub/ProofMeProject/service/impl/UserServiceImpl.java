package com.proofme.manspitub.ProofMeProject.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proofme.manspitub.ProofMeProject.exception.UserAlreadyEnabledException;
import com.proofme.manspitub.ProofMeProject.exception.UserAlreadyExistsException;
import com.proofme.manspitub.ProofMeProject.model.User;
import com.proofme.manspitub.ProofMeProject.repository.UserRepository;
import com.proofme.manspitub.ProofMeProject.security.dto.CreateUserDto;
import com.proofme.manspitub.ProofMeProject.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return this.userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Credenciales Erróneas"));

	}

	public User findUserByEmail(String email) {
		return userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese correo"));
	}

	public String confirmUserAccount(UsernamePasswordAuthenticationToken authentication) {
		String email = authentication.getName(); // Extraemos el email del usuario

		// Buscar usuario por email
		Optional<User> userOpt = userRepository.findFirstByEmail(email);
		if (userOpt.isEmpty()) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		User user = userOpt.get();

		if (user.getEmailConfirmed()) {
			throw new UserAlreadyEnabledException("Ya has confirmado tu email");
		}

		user.setEmailConfirmed(true);
		userRepository.save(user);

		return "Cuenta activada con éxito";
	}

	private void checkEmail(CreateUserDto user) throws Exception {
		if (userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("El email ya esta registrado");
		}
	}

}
