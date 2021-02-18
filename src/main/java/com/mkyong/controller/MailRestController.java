package com.mkyong.controller;

import com.mkyong.model.Mail;
import com.mkyong.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller()
public class MailRestController {
    private final MailService mailService;

    public MailRestController(MailService mailService) {
        this.mailService = mailService;
    }


    @GetMapping
    public String test(Model model) {
        model.addAttribute("message", "Dar Andrei merge rau");
        return "index";
    }

    @GetMapping("/sendMessage")
    public String newDriver(Model model) {
        model.addAttribute("mail", new Mail());
        return "sendMessage";
    }

    @PostMapping
    public String testAdd(@ModelAttribute("mail") Mail mail) {
        System.out.println(mail.getSubject() + " " + mail.getMessage());
        mailService.setMail(mail);
        String host = "pop.gmail.com";// change accordingly
        String mailStoreType = "pop3";
        String username = "tedsparrowmd@gmail.com";// change accordingly
        String password = "NCvbTcJK7w7wxxN";// change accordingly
//        mailService.sendEmail();
        mailService.check(host,mailStoreType,username,password);
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
