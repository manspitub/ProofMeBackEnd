package com.proofme.manspitub.ProofMeProject.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Habitos")
public class Habit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "El usuario propietario es obligatorio")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private User user;

	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
	@Column(nullable = false, length = 100, name = "nombre")
	private String name;

	@Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
	@Column(name = "descripcion", length = 500)
	private String description;

	@NotNull
	@Column(nullable = false, updatable = false, name = "fecha_creacion")
	private Date createdAt = new Date();

	@NotNull(message = "La frecuencia es obligatoria")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "frecuencia")
	private HabitFrequency frequency;

	@NotNull(message = "El estado del hábito es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "estado")
	private HabitStatus status;

	@NotNull(message = "El tipo del hábito es obligatorio")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "tipo")
	private HabitType type;

	@OneToMany(mappedBy = "habit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Relapse> relapses;

	@OneToMany(mappedBy = "habit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SupporterAssignment> supporters;

	// ------------------------------
	// CONSTRUCTORES
	// ------------------------------

	public Habit() {
	}

	public Habit(User user, String name, String description, HabitFrequency frequency, HabitStatus status,
			HabitType type, List<Relapse> relapses, List<SupporterAssignment> supporters) {
		this.user = user;
		this.name = name;
		this.description = description;
		this.frequency = frequency;
		this.status = status;
		this.type = type;
		this.relapses = relapses;
		this.supporters = supporters;
	}

	// ------------------------------
	// GETTERS Y SETTERS
	// ------------------------------

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public HabitFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(HabitFrequency frequency) {
		this.frequency = frequency;
	}

	public HabitStatus getStatus() {
		return status;
	}

	public void setStatus(HabitStatus status) {
		this.status = status;
	}

	public HabitType getType() {
		return type;
	}

	public void setType(HabitType type) {
		this.type = type;
	}

	public List<Relapse> getRelapses() {
		return relapses;
	}

	public void setRelapses(List<Relapse> relapses) {
		this.relapses = relapses;
	}

	public List<SupporterAssignment> getSupporters() {
		return supporters;
	}

	public void setSupporters(List<SupporterAssignment> supporters) {
		this.supporters = supporters;
	}
	
	
}
