package com.ucan.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender mailSender;
  @Value("${app.frontend-url}")
  private String frontendUrl;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendVerificationEmail(String to, String token) {
    var msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject("Verify your UCAN email");
    msg.setText("Welcome to UCAN. Verify: " + frontendUrl + "/verify-email?token=" + token);
    mailSender.send(msg);
  }

  public void sendBonusConfirmation(String to, BigDecimal amount) {
    var msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject("Verification Bonus Credited");
    msg.setText("Your verification bonus of R$ " + amount + " was credited once.");
    mailSender.send(msg);
  }

  public void sendOrderConfirmation(String to, String orderCode, BigDecimal total) {
    var msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject("UCAN Fictional Purchase Confirmation " + orderCode);
    msg.setText("Order " + orderCode + " confirmed. Fictional amount: R$ " + total + ".");
    mailSender.send(msg);
  }
}
