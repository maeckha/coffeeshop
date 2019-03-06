package de.htwg.swqs.order.mail;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;


import org.junit.Rule;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.Session;

import static org.junit.Assert.assertEquals;

/**
 * With this class we can test the email service implementation.
 * For testing this unit we use a local fake smtp mail server, used as a sandbox.
 * Further information's:
 *
 * https://github.com/greenmail-mail-test/greenmail
 *
 */
public class EmailServiceTest {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Test
    public void testSend() throws MessagingException {

        // setup
        Session smtpSession = greenMail.getSmtp().createSession();
        EmailService emailService = new EmailServiceImpl(smtpSession);
        String to = "test@example.com";
        String subject = "Test";
        String content = "some test content...";

        // execute
        emailService.sendMail(to, subject, content);

        // verify
        assertEquals(1, greenMail.getReceivedMessages().length);
        assertEquals(subject, greenMail.getReceivedMessages()[0].getSubject());
        assertEquals("Web shop <webshop@swqs.org>", greenMail.getReceivedMessages()[0].getFrom()[0].toString());
        assertEquals("\"test@example.com\" <test@example.com>", greenMail.getReceivedMessages()[0].getAllRecipients()[0].toString());

        /*
         * the body can not be verified simply because it's a MIME Email with multipart message.
         * SimpleJavaMail can't send plain text messages -> https://github.com/bbottema/simple-java-mail/issues/133
         * and GreenMail can't parse natively the mime multipart messages -> https://github.com/greenmail-mail-test/greenmail/issues/146#issuecomment-227587329
         *
         */
    }
}

