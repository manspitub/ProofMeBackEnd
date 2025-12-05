package com.proofme.manspitub.ProofMeProject.model;

import java.util.Date;
import java.util.List;

import com.proofme.manspitub.ProofMeProject.enums.Role;
import com.proofme.manspitub.ProofMeProject.enums.UserScore;

import ch.qos.logback.core.read.ListAppender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Usuario")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
	@Column(name = "nombre", nullable = false, length = 100)
	private String name;

	@NotBlank(message = "El apellido no puede estar vacío")
	@Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres")
	@Column(name = "apellido", nullable = false, length = 100)
	private String surname;

	@NotBlank(message = "El correo no puede estar vacío")
	@Email(message = "Debe ser un email válido")
	@Column(name = "correo", nullable = false, unique = true)
	private String email;

	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Column(name = "contrasena", nullable = false)
	private String password;

	@Column(name = "fecha_creacion", updatable = false, nullable = false)
	private Date createdAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "rol", nullable = false)
	private Role role;

	@Size(max = 255, message = "La URL de la imagen no puede exceder 255 caracteres")
	@Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "La URL de la imagen debe ser válida (http, https o ftp)")
	@Column(name = "url_imagen")
	private String imageURL;

	@Column(name = "descripcion", length = 500)
	private String description;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private UserScore userScore;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Habit> habits;

	// Assignments como supporter
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SupporterAssignment> supporterAssignments;

	// Pruebas validadas
	@OneToMany(mappedBy = "validatedBy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Proof> validatedProofs;

	// Lista de notificaciones
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications;

	// --------------------
	// Constructores
	// --------------------

	// Constructor vacío
	public User() {
	}

	public User(String name, String surname, String email, String password, String imageURL, String description,
			Role role) {

		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.imageURL = imageURL;
		this.description = description;
		this.role = role;
		this.createdAt = new Date();
	}

	// --------------------
	// Getters y Setters
	// --------------------
	public Long getId() {
		return id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public List<Habit> getHabits() {
		return habits;
	}

	public void setHabits(List<Habit> habits) {
		this.habits = habits;
	}

	public List<SupporterAssignment> getSupporterAssignments() {
		return supporterAssignments;
	}

	public void setSupporterAssignments(List<SupporterAssignment> supporterAssignments) {
		this.supporterAssignments = supporterAssignments;
	}

	public List<Proof> getValidatedProofs() {
		return validatedProofs;
	}

	public void setValidatedProofs(List<Proof> validatedProofs) {
		this.validatedProofs = validatedProofs;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	// --------------------
	// Hooks
	// --------------------

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

}
