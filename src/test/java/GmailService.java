import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class GmailService {

    private static final String APPLICATION_NAME = "My Application";
    private static final String USER_ID = "ninny21051992@gmail.com";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";
    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\ninog\\Desktop\\QA-Automation\\FinalProject\\src\\test\\resources\\client_secret_831007037024-kd2jmmsma32c1fs1dr07g56cm0ejdj91.apps.googleusercontent.com.json";
    private static final String username = "ninny21051992@gmail.com";
    private static final String password = "aisxejvaxeiulrtw";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);



    private static Gmail service;

    static {
        try {
            service = buildService();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public GmailService(String sender, String senderPassword) {
        sender = username;
        senderPassword = password;

    }



    private static Gmail buildService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH), HTTP_TRANSPORT, JSON_FACTORY).createScoped(SCOPES).setAccessToken(ACCESS_TOKEN);

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }



    private static Session connectToSmtp() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        return Session.getInstance(props, authenticator);
    }


    public static void sendEmailWithAttachment(String to, String subject, String body, String attachmentFilePath) throws MessagingException {
        try (Transport transport = connectToSmtp().getTransport()) {

            MimeMessage message = createEmailWithAttachment(to, subject, body, attachmentFilePath);

            // send the email
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Unable to send email", e);
        }

    }

    private static MimeMessage createEmailWithAttachment(String to, String subject, String body, String attachmentFilePath) throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress("your_email_address@gmail.com"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachmentFilePath);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(new File(attachmentFilePath).getName());
        multipart.addBodyPart(attachmentBodyPart);

        email.setContent(multipart);

        return email;
    }

    private static void sendEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        Message message = new Message();
        message.setRaw(Base64.encodeBase64URLSafeString(bytes.toByteArray()));
        service.users().messages().send(USER_ID, message).execute();
    }
}

















