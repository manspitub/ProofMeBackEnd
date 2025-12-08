package com.proofme.manspitub.ProofMeProject.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proofme.manspitub.ProofMeProject.dto.UserDtoConverter;
import com.proofme.manspitub.ProofMeProject.enums.Role;
import com.proofme.manspitub.ProofMeProject.exception.InvalidPasswordException;
import com.proofme.manspitub.ProofMeProject.exception.UserAlreadyEnabledException;
import com.proofme.manspitub.ProofMeProject.exception.UserAlreadyExistsException;
import com.proofme.manspitub.ProofMeProject.model.User;
import com.proofme.manspitub.ProofMeProject.repository.UserRepository;
import com.proofme.manspitub.ProofMeProject.security.dto.CreateUserDto;
import com.proofme.manspitub.ProofMeProject.service.EmailService;
import com.proofme.manspitub.ProofMeProject.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDtoConverter converter;

	@Autowired
	private EmailService emailService;

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

	@Override
	public User addUser(CreateUserDto user) throws Exception {

		checkEmail(user);
		checkPass(user);

		User newUser = new User();
		newUser.setAge(user.getAge());
		newUser.setImageURL(user.getImageURL());
		newUser.setEmailConfirmed(false);
		newUser.setEmail(user.getEmail());
		newUser.setName(user.getName());
		newUser.setSurname(user.getSurname());
		newUser.setDescription(user.getDescription());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));

		// Verifica si hay un usuario logueado y si es ADMIN
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {

			// Recuperar el usuario autenticado
			String currentEmail = authentication.getName();
			User currentUser = userRepository.findFirstByEmail(currentEmail).orElse(null);

			if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
				newUser.setRole(Role.SUPPORTER); // Solo si el logueado es ADMIN
			}
		}

		return userRepository.save(newUser);
	}

	// HELPERS

	private void checkPass(CreateUserDto user) throws Exception {
		if (!user.getPassword().equals(user.getPassword2())) {
			throw new InvalidPasswordException("Las contraseñas no coinciden");
		}

		// Expresión regular para validar la seguridad de la contraseña
		String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

		if (!user.getPassword().matches(passwordRegex)) {
			throw new InvalidPasswordException(
					"La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, un número y un carácter especial.");
		}
	}

	private void checkEmail(CreateUserDto user) throws Exception {
		if (userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("El email ya esta registrado");
		}
	}

}
