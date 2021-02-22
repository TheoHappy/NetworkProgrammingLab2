package com.mkyong.controller;

import com.mkyong.model.EmailMessage;
import com.mkyong.model.Mail;
import com.mkyong.service.MailService;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Controller()
public class MailRestController {
    private final MailService mailService;

    public MailRestController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping
    public String newMessage(Model model) {
        model.addAttribute("mail", new Mail());
        return "sendMessage";
    }

    @GetMapping("inbox")
    public String showInbox(Model model) throws Exception {

        List<EmailMessage> messageList = mailService.getListOfMessages();
        messageList.forEach(System.out::println);
        model.addAttribute("messageList",messageList);
        return "inbox";
    }
    @GetMapping("inbox/message/{id}")
    public String showMessage(@PathVariable(value = "id") int id, Model model) throws Exception {
        List<EmailMessage> messageList = mailService.getListOfMessages();
        EmailMessage message = messageList.get(id);
        model.addAttribute("message",message);
        return "show";
    }


    @PostMapping
    public String testAdd(@ModelAttribute("mail") Mail mail) throws MessagingException {
        System.out.println(mail.getSubject() + " " + mail.getMessage());
        mailService.setMail(mail);
//        String host = "pop.gmail.com";// change accordingly
//        String mailStoreType = "pop3";
//        String username = "tedsparrowmd@gmail.com";// change accordingly
//        String password = "NCvbTcJK7w7wxxN";// change accordingly
        mailService.sendEmail();
//        mailService.check(host,mailStoreType,username,password);
        mailService.getListOfMessages();
//        try {
//            mailService.sendEmail();
////            mailService.sendEmailWithAttachment();
//            System.out.println("Bravo");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return "redirect:/sendMessage";
    }
}
