package com.proofme.manspitub.ProofMeProject.security.jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.proofme.manspitub.ProofMeProject.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtProvider {
	
	public static final String TOKEN_TYPE = "JWT";
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";	
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.duration:3600}") // 1 hora (3600 seconds)
	private int jwtLifeInSeconds;
	
	private JwtParser parser;

	private Key key;
	
	
	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}
	
	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		Map<String, Object> payload = new HashMap<String, Object>();

		payload.put("email", user.getEmail());
		payload.put("role", user.getRole().toString());

		Date tokenExpirationDate = Date
				.from(LocalDateTime.now().plusSeconds(jwtLifeInSeconds).atZone(ZoneId.systemDefault()).toInstant());
		return "Bearer " + Jwts.builder().subject(user.getEmail()).issuedAt(tokenExpirationDate).claims(payload)
				.signWith(key).compact();
	}
	
	public UsernamePasswordAuthenticationToken decodeToken(String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			throw new MalformedJwtException("Formato de token incorrecto");
		}

		// Eliminar "Bearer " del token
		token = token.substring(7);

		// Decodificar el JWT
		Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
				.parseSignedClaims(token).getPayload();

		// Extraer datos del token
		String username = claims.getSubject();
		String role = (String) claims.get("role");

		// Crear la lista de autoridades
		List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

		// Retornar el objeto de autenticación con credenciales null (Spring maneja la
		// autenticación)
		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}
	
	public UsernamePasswordAuthenticationToken getAuthentication(String token) {

		Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
				.parseSignedClaims(token.substring(7)).getPayload();

		String username = claims.getSubject();
		String role = claims.get("role", String.class); // Extrae el rol correctamente

		List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}
	

	
}
