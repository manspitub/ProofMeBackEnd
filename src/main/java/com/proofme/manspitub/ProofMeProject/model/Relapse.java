package com.proofme.manspitub.ProofMeProject.model;

import java.util.Date;

import com.proofme.manspitub.ProofMeProject.enums.RelapseImpact;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Recaidas")
public class Relapse {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // Hábito asociado
	    @NotNull(message = "El hábito asociado es obligatorio")
	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "habito_id", nullable = false)
	    private Habit habit;

	    // Fecha
	    @NotNull(message = "La fecha es obligatoria")
	    @Column(nullable = false, name = "fecha")
	    private Date date = new Date();

	    // Descripción breve
	    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
	    @Column(length = 300, name = "descipcion")
	    private String description;

	    // Impacto en el progreso
	    @NotNull(message = "El impacto es obligatorio")
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false, length = 20, name = "impacto")
	    private RelapseImpact impact;
	    
	    public Relapse() {}

	    public Relapse(Habit habit, Date date, String description, RelapseImpact impact) {
	        this.habit = habit;
	        this.date = date;
	        this.description = description;
	        this.impact = impact;
	    }

	    // ---------------------------
	    // GETTERS Y SETTERS
	    // ---------------------------

	    public Long getId() {
	        return id;
	    }

	    public Habit getHabit() {
	        return habit;
	    }

	    public void setHabit(Habit habit) {
	        this.habit = habit;
	    }

	    public Date getDate() {
	        return date;
	    }

	    public void setDate(Date date) {
	        this.date = date;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public RelapseImpact getImpact() {
	        return impact;
	    }

	    public void setImpact(RelapseImpact impact) {
	        this.impact = impact;
	    }
	    
	    


}
