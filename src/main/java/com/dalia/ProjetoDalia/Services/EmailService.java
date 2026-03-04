package com.dalia.ProjetoDalia.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarDenuncia(String conteudo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo("araujog175@gmail.com");
        mensagem.setSubject("Denuncia Dalia");
        mensagem.setText(conteudo);

        mailSender.send(mensagem);
    }

    public void sendToken(String to, String token){
        SimpleMailMessage message =  new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Codigo de verificação - Dalia");
        message.setText("Olá! Seu codgio de verificação para o app Dalia Calendario menstrual é:"
                        + token +
                        "\nEste codigo expira em 15 minutos.");
        mailSender.send(message);
    }
}

