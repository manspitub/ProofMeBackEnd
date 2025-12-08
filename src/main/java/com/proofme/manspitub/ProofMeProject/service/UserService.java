package com.proofme.manspitub.ProofMeProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proofme.manspitub.ProofMeProject.dto.UserDtoConverter;
import com.proofme.manspitub.ProofMeProject.model.User;
import com.proofme.manspitub.ProofMeProject.repository.UserRepository;
import com.proofme.manspitub.ProofMeProject.security.dto.CreateUserDto;

public interface UserService extends UserDetailsService {
	
	UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

	User addUser(CreateUserDto user) throws Exception;
	
	String confirmUserAccount(UsernamePasswordAuthenticationToken authentication);

}
