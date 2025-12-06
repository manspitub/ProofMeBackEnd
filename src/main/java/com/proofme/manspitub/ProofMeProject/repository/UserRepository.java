package com.proofme.manspitub.ProofMeProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proofme.manspitub.ProofMeProject.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findFirstByEmail(String email);

}
