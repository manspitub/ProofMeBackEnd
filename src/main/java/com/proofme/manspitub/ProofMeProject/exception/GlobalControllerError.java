package com.proofme.manspitub.ProofMeProject.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerError {

	@ExceptionHandler(value = EmailNotConfirmedException.class)
	public ResponseEntity<ApiError> handleEmailNotConfirmedException(EmailNotConfirmedException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler(UserAlreadyEnabledException.class)
	public ResponseEntity<ApiError> handleUserAlreadyEnabledException(UserAlreadyEnabledException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	@ExceptionHandler(value = InvalidPasswordException.class)
	public ResponseEntity<ApiError> handleInvalidPasswordException(InvalidPasswordException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

}
