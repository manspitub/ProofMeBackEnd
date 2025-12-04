package com.proofme.manspitub.ProofMeProject.model;

import java.io.Serializable;
import java.util.Objects;

public class SupporterAssignmentPK implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private Long habit; 
	private Long user;
	
	
	public SupporterAssignmentPK() {
		super();
	}


	public SupporterAssignmentPK(Long habit, Long user) {
		super();
		this.habit = habit;
		this.user = user;
	}
	


	public Long getHabit() {
		return habit;
	}


	public void setHabit(Long habit) {
		this.habit = habit;
	}


	public Long getUser() {
		return user;
	}


	public void setUser(Long user) {
		this.user = user;
	}


	@Override
	public int hashCode() {
		return Objects.hash(habit, user);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupporterAssignmentPK other = (SupporterAssignmentPK) obj;
		return Objects.equals(habit, other.habit) && Objects.equals(user, other.user);
	}
	
	
}
