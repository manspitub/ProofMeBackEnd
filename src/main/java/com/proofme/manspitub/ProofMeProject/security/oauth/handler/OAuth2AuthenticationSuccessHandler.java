package com.proofme.manspitub.ProofMeProject.security.oauth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.proofme.manspitub.ProofMeProject.security.jwt.JwtProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private JwtProvider jwtProvider;

	private final String frontendUrl;

	public OAuth2AuthenticationSuccessHandler(JwtProvider jwtProvider,
			@Value("${app.frontend.url}") String frontendUrl) {
		this.jwtProvider = jwtProvider;
		this.frontendUrl = frontendUrl;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String token = jwtProvider.generateToken(authentication);

		// Detectar si es Postman (opcional)
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null && userAgent.contains("Postman")) {
			// Devolver JWT en JSON
			response.setContentType("application/json");
			response.getWriter().write("{\"token\": \"" + token + "\"}");
			return;
		}

		// Para frontend normal
		String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl).queryParam("token", token).build()
				.toUriString();
		response.sendRedirect(targetUrl);
	}
}
