package com.mrlee.free_board.common;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyMailSender {

    private final JavaMailSender javaMailSender;

    private final static String receiver = "lkt0520@naver.com";

    public void sendMail(MyMailMessage myMailMessage) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(myMailMessage.getSubject());
        simpleMailMessage.setText(myMailMessage.getText());
        simpleMailMessage.setTo(receiver);
        javaMailSender.send(simpleMailMessage);
    }
}
