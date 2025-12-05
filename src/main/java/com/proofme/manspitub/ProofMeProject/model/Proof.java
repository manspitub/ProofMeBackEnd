package com.proofme.manspitub.ProofMeProject.model;

import java.util.Date;

import com.proofme.manspitub.ProofMeProject.enums.ProofStatus;
import com.proofme.manspitub.ProofMeProject.enums.ProofType;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Pruebas")
public class Proof {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relación con hábito ---
    @NotNull(message = "El hábito asociado es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habito_id", nullable = false)
    private Habit habit;

    // --- Fecha de subida ---
    @NotNull(message = "La fecha de subida es obligatoria")
    @Column(nullable = false, updatable = false, name = "fecha_subida")
    private Date uploadDate = new Date();

    // --- Tipo de prueba ---
    @NotNull(message = "El tipo de prueba es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, name = "tipo")
    private ProofType type;

 // --- Archivo de evidencia ---
    @NotNull(message = "La evidencia es obligatoria")
    @Size(min = 5, max = 500, message = "La URL debe tener entre 5 y 500 caracteres")
    @Pattern(
        regexp = "^(https?://).+",
        message = "La evidencia debe ser una URL válida que comience con http:// o https://"
    )
    @Column(nullable = false, length = 500, name = "url")
    private String evidenceUrl;

    // --- Estado ---
    @NotNull(message = "El estado de la prueba es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, name = "estado")
    private ProofStatus status;

    // --- Comentario del supporter ---
    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    @Column(name = "comentario")
    private String supporterComment;

    // --- Puntuación obtenida ---
    @Min(value = 0, message = "La puntuación mínima es 0")
    @Max(value = 100, message = "La puntuación máxima es 100")
    @Column(name="puntuacion")
    private Integer score;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supporter_id")
    private User validatedBy; // solo el supporter asignado puede validar
    
    public Proof() {}
    public Proof(Habit habit, ProofType type, String evidenceUrl, ProofStatus status) {
        this.habit = habit;
        this.type = type;
        this.evidenceUrl = evidenceUrl;
        this.status = status;
    }
    
    // ---------------------------------------------------
    // GETTERS Y SETTERS
    // ---------------------------------------------------

    public Long getId() {
        return id;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public ProofType getType() {
        return type;
    }

    public void setType(ProofType type) {
        this.type = type;
    }

    public String getEvidenceUrl() {
        return evidenceUrl;
    }

    public void setEvidenceUrl(String evidenceUrl) {
        this.evidenceUrl = evidenceUrl;
    }

    public ProofStatus getStatus() {
        return status;
    }

    public void setStatus(ProofStatus status) {
        this.status = status;
    }

    public String getSupporterComment() {
        return supporterComment;
    }

    public void setSupporterComment(String supporterComment) {
        this.supporterComment = supporterComment;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
	public User getValidatedBy() {
		return validatedBy;
	}
	public void setValidatedBy(User validatedBy) {
		this.validatedBy = validatedBy;
	}
    
    
	
}
