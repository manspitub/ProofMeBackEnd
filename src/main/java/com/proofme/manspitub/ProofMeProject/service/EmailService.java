package com.proofme.manspitub.ProofMeProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendConfirmationEmail(String to, String name, String surname, String token, boolean isSupporter,
			String rawPassword) throws MessagingException {

		String subject = isSupporter ? "👁️ Has sido registrado como Supervisor en ProofMe"
				: "🔥 ¡Activa tu cuenta y comienza tu camino en ProofMe!";

		String confirmationUrl = "http://localhost:4200/verify-email?token=" + token;

		String brandColor = "#F4A938"; // NARANJA PROOFME
		String darkColor = "#333333";
		String softBg = "#FFF7EB";

		StringBuilder content = new StringBuilder();
		content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 25px; ")
				.append("border: 1px solid #e0e0e0; border-radius: 12px; background-color: ").append(softBg)
				.append("; text-align: center;'>")

				.append("<h2 style='color: ").append(darkColor).append(";'>").append("👁️ ¡Hola ").append(name)
				.append(" ").append(surname).append("!</h2>");

		if (isSupporter) {
			content.append("<p style='font-size: 16px; color: ").append(darkColor).append(";'>").append(
					"Has sido registrado como <strong>Supervisor</strong> en <strong>ProofMe</strong>. Podrás validar hábitos, revisar pruebas y ayudar a otras personas a mantener su progreso.</p>")
					.append("<p style='font-size: 16px; color: ").append(darkColor)
					.append(";'>Estos son tus datos de acceso:</p>")
					.append("<ul style='list-style: none; padding: 0; font-size: 16px; color: ").append(darkColor)
					.append(";'>").append("<li><strong>Correo electrónico:</strong> ").append(to).append("</li>")
					.append("<li><strong>Contraseña:</strong> ").append(rawPassword).append("</li>").append("</ul>");
		} else {
			content.append("<p style='font-size: 16px; color: ").append(darkColor).append(
					";'>Gracias por unirte a <strong>ProofMe</strong>. Antes de comenzar a construir hábitos, ganar recompensas y demostrar tu progreso con pruebas reales, necesitamos que actives tu cuenta.</p>")
					.append("<p style='font-size: 16px; color: ").append(darkColor)
					.append(";'>Haz clic en el botón para continuar:</p>");
		}

		// BOTÓN
		content.append("<a href='").append(confirmationUrl)
				.append("' style='display: inline-block; padding: 14px 25px; margin: 20px 0; font-size: 17px; ")
				.append("color: #fff; background-color: ").append(brandColor)
				.append("; text-decoration: none; border-radius: 6px; font-weight: bold;'>")
				.append("⚡ Activar mi cuenta</a>")

				// EXTRA
				.append("<p style='font-size: 14px; color: #777;'>Si no solicitaste una cuenta en ProofMe, ignora este correo.</p>")
				.append("<hr style='margin: 25px 0; border: none; height: 1px; background-color: #e6e6e6;'>")

				.append("<p style='font-size: 14px; color: #999;'>Con constancia todo mejora.<br><strong>Equipo ProofMe 👁️</strong></p>")
				.append("</div>");

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content.toString(), true);

		mailSender.send(message);
	}

}
