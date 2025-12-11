package com.proofme.manspitub.ProofMeProject.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerError {

	// Excepción lanzada cuando el usuario intenta iniciar sesión sin haber
	// confirmado el email
	@ExceptionHandler(value = EmailNotConfirmedException.class)
	public ResponseEntity<ApiError> handleEmailNotConfirmedException(EmailNotConfirmedException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	// Excepción lanzada si el usuario intenta habilitar un usuario ya habilitado
	// previamente
	@ExceptionHandler(UserAlreadyEnabledException.class)
	public ResponseEntity<ApiError> handleUserAlreadyEnabledException(UserAlreadyEnabledException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	// Excepción lanzada cuando se intenta registrar un usuario que ya existe en el
	// sistema
	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	// Excepción lanzada cuando la firma del JWT es inválida o manipulada
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ApiError> handleSignatureException(SignatureException e) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), "Firma del token inválida");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}

	// Excepción lanzada cuando la contraseña no cumple los requisitos definidos
	@ExceptionHandler(value = InvalidPasswordException.class)
	public ResponseEntity<ApiError> handleInvalidPasswordException(InvalidPasswordException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	// Excepciones lanzadas por validaciones de campos (anotaciones @NotBlank,
	// @Email, etc.)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
		String errors = e.getConstraintViolations().stream()
				.map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
				.collect(Collectors.joining(", "));
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	// Excepción lanzada por violaciones en la base de datos (como claves
	// duplicadas)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		String message = "Error de integridad de datos";

		if (e.getRootCause() != null && e.getRootCause().getMessage() != null
				&& e.getRootCause().getMessage().contains("Duplicate entry")) {
			message = "El valor que intentas registrar ya existe en el sistema";
		}

		ApiError apiError = new ApiError(HttpStatus.CONFLICT, LocalDateTime.now(), message);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	// Excepción lanzada cuando las credenciales del usuario no son válidas
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handleUnauthorizedException(BadCredentialsException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(),
				"Usuario y/o contraseña incorrecto(s)");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	// Excepción lanzada cuando el cuerpo de la petición está vacío o mal formado
	// (JSON incorrecto)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(),
				"El cuerpo de la petición es obligatorio o está mal formado");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	// Excepción lanzada cuando el usuario no existe en la base de datos durante el
	// login
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(),
				"El usuario no existe o no está registrado en el sistema");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	// Excepción lanzada si no se puede enviar el correo al usuario
	@ExceptionHandler(MailSendException.class)
	public ResponseEntity<ApiError> handleMailSendException(MailSendException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"No se pudo enviar el correo. Inténtalo más tarde.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	// Excepción lanzada cuando hay un error al construir el contenido del correo
	// electrónico
	@ExceptionHandler(MailParseException.class)
	public ResponseEntity<ApiError> handleMailParseException(MailParseException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"Error al construir el contenido del correo.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	// Excepción lanzada cuando falla la autenticación con el servidor SMTP
	@ExceptionHandler(MailAuthenticationException.class)
	public ResponseEntity<ApiError> handleMailAuthenticationException(MailAuthenticationException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"No se pudo autenticar con el servidor de correo. Contacta con el administrador.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	// Excepción lanzada cuando el token JWT está malformado o corrupto
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ApiError> handleMalformedJwtException(MalformedJwtException e) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}


	// Excepción lanzada cuando la ruta solicitada no existe
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> handleNoHandlerFoundException(
			org.springframework.web.servlet.NoHandlerFoundException e) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(),
				"La ruta solicitada no existe: " + e.getRequestURL());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	// Excepción lanzada cuando el método HTTP no está permitido (GET, POST, etc.)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiError> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

		String supportedMethods = (e.getSupportedHttpMethods() != null)
				? e.getSupportedHttpMethods().stream().map(HttpMethod::name) // ← CORRECCIÓN AQUÍ
						.collect(Collectors.joining(", "))
				: "Ninguno";

		String message = "Método HTTP no permitido. Métodos soportados: " + supportedMethods;

		ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, LocalDateTime.now(), message);

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiError);
	}

	// Excepción genérica (cualquier error no contemplado arriba)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"Ha ocurrido un error inesperado. Contacta con el administrador.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	// Maneja la excepción cuando el token de Google es inválido o expirado
	@ExceptionHandler(InvalidGoogleTokenException.class)
	public ResponseEntity<ApiError> handleInvalidGoogleToken(InvalidGoogleTokenException e) {

		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}

	// Maneja errores al obtener la información del usuario desde Google
	@ExceptionHandler(GoogleUserInfoException.class)
	public ResponseEntity<ApiError> handleGoogleUserInfoException(GoogleUserInfoException e) {

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

}
