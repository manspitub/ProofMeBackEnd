package com.proofme.manspitub.ProofMeProject.security.dto;

public class CreateUserDto {
	private String name;
	
	private String surname;
	
	private String email;
	
	private Integer age;
	
	private String role;
	
	private String imageURL;
	
	private String description;
	
	private String password;
	
	private String password2;

	public CreateUserDto() {
		super();
	}

	public CreateUserDto(String name, String surname, String email, Integer age, String role, String imageURL,
			String description, String password, String password2) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.age = age;
		this.role = role;
		this.imageURL = imageURL;
		this.description = description;
		this.password = password;
		this.password2 = password2;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	
	
}
