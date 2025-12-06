package com.proofme.manspitub.ProofMeProject.dto;

import org.springframework.stereotype.Component;

import com.proofme.manspitub.ProofMeProject.model.User;

@Component
public class UserDtoConverter {
	
	public UserDto convertUserDto(User user) {
		return new UserDto(
			    user.getId(),
			    user.getName(),
			    user.getSurname(),
			    user.getEmail(),
			    user.getAge(),
			    user.getEmailConfirmed(),
			    user.getCreatedAt(),
			    user.getRole().toString(),
			    user.getImageURL(),
			    user.getDescription()
			);
}}
