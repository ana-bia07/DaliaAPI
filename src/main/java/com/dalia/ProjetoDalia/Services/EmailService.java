package com.dalia.ProjetoDalia.Services;


import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private Resend resend;
    @Value("${API_KEY}")
    private String apiKey;

    @Async
    //logica da denuncia que envia o email pra delegacia da mulher (vulgo guilherme)
    public void enviarDenuncia(String conteudo) {
        resend = new Resend(apiKey);
        CreateEmailOptions params = CreateEmailOptions.builder()
                .to("playy.story22@gmail.com")
                .subject("ALERTA DE SEGURANÇA - Usuaria Dalia solciita apoio")
                .html(conteudo)
                .build();
        try{
            resend.emails().send(params);
            System.out.println("Email enviado com sucesso!");
        } catch (ResendException e) {
            System.out.println("Email enviado com erro!" + e.getMessage());
        }
    }

    @Async
    //envia o email com o codigo para a usuaria
    public void sendToken(String to, String token){
        resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
            .from("Dalia <onboarding@resend.dev>")
            .to(to)
            .subject("Codigo de verificação - Dalia")
            .html("Olá! \nSeu codigo de verificação para o app Dalia Calendario menstrual é: \n"
            + token +
            "\nEste codigo expira em 15 minutos.")
            .build();
        try{
            resend.emails().send(params);
            System.out.println("Email enviado com sucesso!");
        } catch (ResendException e) {
            System.out.println("Email enviado com erro!" + e.getMessage());
        }
    }
}

