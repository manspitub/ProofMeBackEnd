package com.proofme.manspitub.ProofMeProject.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.proofme.manspitub.ProofMeProject.security.jwt.JwtAccessDeniedHandler;
import com.proofme.manspitub.ProofMeProject.security.jwt.JwtAuthorizationFilter;
import com.proofme.manspitub.ProofMeProject.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.proofme.manspitub.ProofMeProject.security.oauth.service.CustomOAuth2UserService;
import com.proofme.manspitub.ProofMeProject.service.UserService;
import com.proofme.manspitub.ProofMeProject.service.impl.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JwtAuthorizationFilter filter;
	@Autowired
	private JwtAccessDeniedHandler accessDeniedHandler;
	 

	@Bean
	UserService userDetailsService() {
	    return new UserServiceImpl();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2AuthenticationSuccessHandler oauth2SuccessHandler,
		    CustomOAuth2UserService oauth2UserService ) throws Exception {

		http
				// Deshabilitar CSRF porque usamos JWT (stateless)
				.csrf(csrf -> csrf.disable())

				// Manejo de excepciones personalizadas
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler))

				// JWT → Sin sesiones
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Autorización de rutas
				.authorizeHttpRequests(auth -> auth
						// Rutas públicas de autenticación
						.requestMatchers("/api/auth/**").permitAll()

						// OAuth2 (público hasta que Google devuelva callback)
						.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

						// Cualquier ruta de tu API requiere autenticación
						.requestMatchers("/api/**").authenticated()

						// Rutas fuera de /api → permitidas (si las hubiera)
						.anyRequest().permitAll())

				// Configuración de OAuth2 con Google
				.oauth2Login(oauth2 -> oauth2.authorizationEndpoint(config -> config.baseUri("/oauth2/authorize"))
						.redirectionEndpoint(config -> config.baseUri("/login/oauth2/code/*"))
						.userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService))
						.successHandler(oauth2SuccessHandler));

		// Filtro JWT antes del filtro de usuario/contraseña de Spring
		return http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// Configuración CORS Global
	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:4200") // MODO DESAROLLO
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
						.allowCredentials(true); // Si necesitas enviar cookies y encabezados de autenticación
			}
		};
	}

}
