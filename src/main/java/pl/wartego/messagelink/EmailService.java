package pl.wartego.messagelink;


import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
      private static Properties properties;

    //provide recipient's email ID
    private String recipient = "wartego@wp.pl";
    //provide sender's email ID
    private String senderEmailBox = "jakartato@javatestwartego.pl";
    protected void getEmailConfigResources() {
        properties = new Properties();
        ClassLoader classLoader = EmailService.class.getClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream("emailconfig.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getEmailResourcesDetails();
    }

    protected void getEmailResourcesDetails() {
        final String auth = properties.getProperty("mail.smtp.auth");
        final String startTLS = properties.getProperty("mail.smtp.starttls.enable");
        final String host = properties.getProperty("mail.smtp.host");
        final String port = properties.getProperty("mail.smtp.port");
        final String username = properties.getProperty("username");
        final String password = properties.getProperty("password");


        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(properties, authenticator);
        try {
            //create a MimeMessage object
            Message message = new MimeMessage(session);
            //set From email field
            message.setFrom(new InternetAddress(senderEmailBox));
            //set To email field
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            //set email subject field
            message.setSubject("Here comes Jakarta Mail!");
            //set the content of the email message
            message.setContent("Just discovered that Jakarta Mail is fun and easy to use", "text/html");
            //send the email message
            Transport.send(message);
            System.out.println("Email Message Sent Successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

}
