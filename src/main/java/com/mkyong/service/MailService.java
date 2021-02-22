package com.mkyong.service;

import com.mkyong.model.EmailMessage;
import com.mkyong.model.Mail;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {
    private Mail mail;

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("teodore473@gmail.com");

        msg.setSubject(mail.getSubject());
        msg.setText(mail.getMessage());

        javaMailSender.send(msg);

    }

    public void sendEmailWithAttachment() throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("teodore473@gmail.com");

        helper.setSubject("Testing from Spring Boot");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>Check attachment for image!</h1>", true);

        //FileSystemResource file = new FileSystemResource(new File("classpath:android.png"));

        //Resource resource = new ClassPathResource("android.png");
        //InputStream input = resource.getInputStream();

        //ResourceUtils.getFile("classpath:android.png");

        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(msg);
    }


    public List<EmailMessage> getListOfMessages() throws MessagingException {
        Folder folder = null;
        Store store = null;
        List<EmailMessage> messageList = new ArrayList<>();

        try {
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com","tedsparrowmd@gmail.com", "NCvbTcJK7w7wxxN");
            folder = store.getFolder("Inbox");

            folder.open(Folder.READ_WRITE);
            Message messages[] = folder.getMessages();
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
            for (int i=0; i < messages.length; ++i) {
                Message message = messages[i];
                messageList.add(new EmailMessage(i,message.getSubject(),""+message.getFrom()[0],readPlainContent((MimeMessage) message)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (folder != null) { folder.close(true); }
            if (store != null) { store.close(); }
        }
        return messageList;
    }

    String readPlainContent(MimeMessage message) throws Exception {
        return new MimeMessageParser(message).parse().getPlainContent();
    }
}
