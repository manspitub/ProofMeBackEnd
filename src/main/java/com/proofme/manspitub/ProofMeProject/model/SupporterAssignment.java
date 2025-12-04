package com.proofme.manspitub.ProofMeProject.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "AsignacionSupporter")
@IdClass(SupporterAssignmentPK.class)
public class SupporterAssignment {
	
	
	
	// --- Habit asociado ---
	@Id
	@NotNull(message = "El hábito asociado es obligatorio")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "habito_id", nullable = false)
	private Habit habit;

	// --- Usuario asociado ---
	@Id
	@NotNull(message = "El usuario asociado es obligatorio")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private User user;

	// --- Fecha de asignación ---
	@NotNull(message = "La fecha de asignación es obligatoria")
	@PastOrPresent(message = "La fecha no puede ser futura")
	@Column(name = "fecha_asignacion", nullable = false, updatable = false)
	private Date assignedAt = new Date();

	// ------------------------
	// CONSTRUCTORES
	// ------------------------

	// Constructor vacío obligatorio para JPA
	public SupporterAssignment() {
	}

	// Constructor con parámetros
	public SupporterAssignment(Habit habit, User user, Date assignedAt) {
		this.habit = habit;
		this.user = user;
		this.assignedAt = assignedAt != null ? assignedAt : new Date();
	}

	// -------- Getters & Setters --------

	
	public Habit getHabit() {
		return habit;
	}

	public void setHabit(Habit habit) {
		this.habit = habit;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getAssignedAt() {
		return assignedAt;
	}

	public void setAssignedAt(Date assignedAt) {
		this.assignedAt = assignedAt;
	}

}
