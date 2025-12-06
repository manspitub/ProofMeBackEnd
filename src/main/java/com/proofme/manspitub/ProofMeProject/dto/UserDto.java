package com.proofme.manspitub.ProofMeProject.dto;

import java.util.Date;


public class UserDto {

	private Long id;

	private String name;

	private String surname;

	private String email;

	private Integer age;

	private Boolean emailConfirmed = false;

	private Date createdAt;

	private String role;

	private String imageURL;

	private String description;

	public UserDto() {
		super();
	}

	public UserDto(Long id, String name, String surname, String email, Integer age, Boolean emailConfirmed,
			Date createdAt, String role, String imageURL, String description) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.age = age;
		this.emailConfirmed = emailConfirmed;
		this.createdAt = createdAt;
		this.role = role;
		this.imageURL = imageURL;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(Boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
