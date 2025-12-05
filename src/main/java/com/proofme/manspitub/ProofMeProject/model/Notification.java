package com.proofme.manspitub.ProofMeProject.model;

import java.util.Date;

import com.proofme.manspitub.ProofMeProject.enums.NotificationStatus;
import com.proofme.manspitub.ProofMeProject.enums.NotificationType;

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

@Entity
@Table(name = "Notificaciones")
public class Notification {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	// --- Usuario asociado ---
    @NotNull(message = "El usuario asociado es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;
    
 // --- Tipo de evento ---
    @NotNull(message = "El tipo de evento es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="tipo")
    private NotificationType type;
    
 // --- Fecha y hora ---
    @NotNull(message = "La fecha y hora son obligatorias")
    @Column(nullable = false)
    private Date timestamp = new Date();
    
 // --- Estado ---
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="estado")
    private NotificationStatus status;
    
    // ------------------------
    // CONSTRUCTORES
    // ------------------------

    public Notification() {}

    public Notification(User user, NotificationType type, Date timestamp, NotificationStatus status) {
        this.user = user;
        this.type = type;
        this.timestamp = timestamp != null ? timestamp : new Date();
        this.status = status != null ? status : NotificationStatus.UNREAD;
    }

	
}
