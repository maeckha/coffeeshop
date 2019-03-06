package de.htwg.swqs.order.mail;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;

/**
 * Testcase for testing our 'real' mail server; the verification is done by calling the rest api
 * from the smtp fake server, the responded json is parsed with jackson
 */
public class EmailServiceIT {

  @Test
  public void sendEmailSuccessful() {

    // setup
    EmailService emailService = new EmailServiceImpl();
    String to = "test@example.com";
    String subject = "Test";
    String content = "some test content...";

    // execute
    emailService.sendMail(to, subject, content);

    // verify
    EmailServerResponse response = callEmailFakeServerToAccessEmail(to);

    assertEquals("webshop@swqs.org", response.sender);
    assertEquals(to, response.recipient);
    assertEquals(subject, response.subject);
  }

  /**
   * Helper method to call the verification api of smtpbucket.com
   *
   * @param recipient The recipient of the email which was sent
   * @return A EmailServerResponse object which contains the information about the sent mail
   */
  private EmailServerResponse callEmailFakeServerToAccessEmail(String recipient) {

    String[] recipientAddress = recipient.split("@");
    EmailServerResponse response = null;

    try {
      // called without tls because of a sslHandShakeException
      URL url = new URL(
          "http://api.smtpbucket.com/emails" + "?" + "sender=webshop%40swqs.org&recipient="
              + recipientAddress[0] + "%40" + recipientAddress[1]);
      System.out.println(url.toString());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setDoOutput(true);

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuilder content = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      connection.disconnect();

      // parsing the result with jackson
      ObjectMapper mapper = new ObjectMapper();
      JsonNode results = mapper.readTree(content.toString());
      JsonNode resultsContent = results.get("results");
      JsonNode latestMailEntry = resultsContent.get(resultsContent.size() - 1);
      response = new EmailServerResponse(
          latestMailEntry.get("sender").asText(),
          latestMailEntry.get("recipients").get(0).asText(),
          latestMailEntry.get("subject").asText()
      );

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return response;

  }

  /**
   * Private wrapper class for the response data from the api call to the smtp server
   */
  private class EmailServerResponse {

    public String sender;
    public String recipient;
    public String subject;

    public EmailServerResponse(String sender, String recipient, String subject) {
      this.sender = sender;
      this.recipient = recipient;
      this.subject = subject;
    }

    @Override
    public String toString() {
      return "EmailServerResponse{" +
          "sender='" + sender + '\'' +
          ", recipient='" + recipient + '\'' +
          ", subject='" + subject + '\'' +
          '}';
    }
  }
}



