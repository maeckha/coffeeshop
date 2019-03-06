package de.htwg.swqs.order.mail;

public interface EmailService {

  void sendMail(String to, String subject, String text);
}
