package com.proofme.manspitub.ProofMeProject.enums;

import java.util.ArrayList;
import java.util.List;

import com.proofme.manspitub.ProofMeProject.model.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Puntuaciones")
public class UserScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// --- Usuario asociado ---
	@NotNull(message = "El usuario asociado es obligatorio")
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false, unique = true)
	private User user;

	// --- Puntos totales ---
	@Min(value = 0, message = "Los puntos totales no pueden ser negativos")
	@Column(nullable = false, name = "puntos_totales")
	private int totalPoints = 0;

	// --- Lista de logros desbloqueados ---
	@ElementCollection
	@CollectionTable(name = "Logros_Usuario", joinColumns = @JoinColumn(name = "usuario_puntuacion_id"))
	@Column(name = "logro")
	private List<String> unlockedAchievements;

	// --- Nivel actual ---
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name="nivel")
	private UserLevel level = UserLevel.BEGINNER;

	public UserScore() {
	}

	public UserScore(User user, int totalPoints, List<String> unlockedAchievements, UserLevel level) {
		this.user = user;
		this.totalPoints = totalPoints;
		this.unlockedAchievements = unlockedAchievements != null ? unlockedAchievements : new ArrayList<>();
		this.level = level != null ? level : UserLevel.BEGINNER;
	}

	// ------------------------
	// GETTERS & SETTERS
	// ------------------------

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public List<String> getUnlockedAchievements() {
		return unlockedAchievements;
	}

	public void setUnlockedAchievements(List<String> unlockedAchievements) {
		this.unlockedAchievements = unlockedAchievements;
	}

	public UserLevel getLevel() {
		return level;
	}

	public void setLevel(UserLevel level) {
		this.level = level;
	}
}
