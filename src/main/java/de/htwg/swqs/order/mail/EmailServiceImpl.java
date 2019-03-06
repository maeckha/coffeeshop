
package de.htwg.swqs.order.mail;

import java.util.Optional;
import javax.mail.Session;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Service;

/**
 * <p> For faking our real mail smtp server we use the free service from
 * https://www.smtpbucket.com/
 *
 * We can send emails to this service and verify that they have reached the outgoing mail server.
 * The service works without any registration (andy warranty) and is fully free of costs.
 *
 * On the following page you can search for mails which was sent: https://www.smtpbucket.com/emails
 * The verification can also be done through a rest api (used in the test cases for this service)
 * </p>
 */
@Service
public class EmailServiceImpl implements EmailService {

  private static final String SENDER_NAME = "Web shop";
  private static final String SENDER_ADDRESS = "webshop@swqs.org";
  private static final String HOST = "mail.smtpbucket.com";
  private static final Integer PORT = 8025;

  // we misuse the optional object to maybe provide a javax.mail.session object,
  // used when sending the mail with the MailerBuilder and needed for testing the code
  private Optional<Session> sessionOptional;

  public EmailServiceImpl() {
    this.sessionOptional = Optional.empty();
  }

  public EmailServiceImpl(Session session) {
    this.sessionOptional = Optional.of(session);
  }


  @Override
  public void sendMail(String to, String subject, String text) {
    Email email = EmailBuilder.startingBlank()
        .from(SENDER_NAME, SENDER_ADDRESS)
        .to(to, to)
        .withSubject(subject)
        .withPlainText(text)
        .buildEmail();

    System.out.println(email.getPlainText());

    processingMail(email);

  }

  /**
   * Sends a predefined email object.
   *
   * @param email The Email which will be sent
   */
  private void processingMail(Email email) {

    if (this.sessionOptional.isPresent()) {
      // for testing purposes do we deliver a custom green mail session
      MailerBuilder
          .usingSession(this.sessionOptional.get())
          .withDebugLogging(true)
          .buildMailer()
          .sendMail(email);

    } else {
      // in production mode we use the default SimpleJavaMail session
      MailerBuilder
          .withSMTPServer(HOST, PORT)
          .buildMailer()
          .sendMail(email);
    }
  }
}
