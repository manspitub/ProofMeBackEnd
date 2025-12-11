package com.proofme.manspitub.ProofMeProject.security.oauth.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.proofme.manspitub.ProofMeProject.enums.Role;
import com.proofme.manspitub.ProofMeProject.exception.GoogleUserInfoException;
import com.proofme.manspitub.ProofMeProject.exception.InvalidGoogleTokenException;

import com.proofme.manspitub.ProofMeProject.model.User;
import com.proofme.manspitub.ProofMeProject.repository.UserRepository;
import com.proofme.manspitub.ProofMeProject.service.UserService;

import tools.jackson.databind.ObjectMapper;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserService userService; // servicio para crear/consultar usuarios
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);
		return processOAuth2User(userRequest, oauth2User);
	}

	/**
	 * Carga o crea el usuario a partir de un Access Token de Google (para Postman)
	 */
	public OAuth2User loadUserFromAccessToken(String accessToken) {
		try {
			String uri = "https://www.googleapis.com/oauth2/v3/userinfo";
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response;
			try {
				response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			} catch (HttpClientErrorException.Unauthorized e) {
				throw new InvalidGoogleTokenException("El access token de Google es inválido o ha expirado");
			} catch (Exception e) {
				throw new GoogleUserInfoException("No se pudo conectar con Google para obtener el usuario");
			}

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> attributes;

			try {
				attributes = mapper.readValue(response.getBody(), Map.class);
			} catch (Exception e) {
				throw new GoogleUserInfoException("No se pudo procesar la información del usuario de Google");
			}

			if (!attributes.containsKey("email")) {
				throw new GoogleUserInfoException("La información recibida de Google no contiene un email válido");
			}

			OAuth2User oauth2User = new DefaultOAuth2User(null, attributes, "email");

			return processOAuth2User(null, oauth2User);

		} catch (RuntimeException e) {
			throw e; // reenviamos excepciones personalizadas
		} catch (Exception e) {
			throw new RuntimeException("Error inesperado obteniendo usuario de Google", e);
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
		Map<String, Object> attributes = oauth2User.getAttributes();

		// Obtenemos datos de la cuenta de Google para el perfil del usuario
		String email = (String) attributes.get("email");
		String givenName = (String) attributes.get("given_name"); // nombre
		String familyName = (String) attributes.get("family_name"); // apellidos
		String picture = (String) attributes.get("picture");
		Boolean emailVerified = attributes.get("email_verified") != null ? (Boolean) attributes.get("email_verified")
				: false;

		// 1. buscar usuario por email
		Optional<User> userOpt = userRepository.findFirstByEmail(email);
		User user;
		if (userOpt.isPresent()) {
			user = userOpt.get();
			// update profile picture, name if needed
			boolean changed = false;
			if (user.getImageURL() == null && picture != null) {
				user.setImageURL(picture);
				changed = true;
			}
			if (user.getName() == null && givenName != null) {
				user.setName(givenName);
				changed = true;
			}
			if (user.getSurname() == null && familyName != null) {
				user.setSurname(familyName);
				changed = true;
			}
			if (changed)
				userRepository.save(user);
		} else {
			// 2. crear nuevo usuario con datos mínimos
			user = new User();
			user.setEmail(email);
			user.setName(givenName != null ? givenName : "");
			user.setSurname(familyName != null ? familyName : "");
			user.setImageURL(picture);
			user.setDescription("Usuario registrado vía Google OAuth2");
			user.setEmailConfirmed(emailVerified); // true si Google lo confirma

			// Generar contraseña aleatoria y hashearla (no utilizada para login con Google)
			String randomPass = UUID.randomUUID().toString();
			user.setPassword(passwordEncoder.encode(randomPass));

			// role por defecto
			user.setRole(Role.USER);
			userRepository.save(user);
		}

		// devolver un OAuth2User
		return oauth2User;
	}

}
