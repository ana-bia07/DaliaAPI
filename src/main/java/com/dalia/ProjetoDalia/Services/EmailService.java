package com.dalia.ProjetoDalia.Services;



import com.dalia.ProjetoDalia.Model.DTOS.Users.BrevoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;
    @Value("${brevo.sender.email}")
    private String senderEmail;
    @Value("${brevo.sender.name}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarDenuncia(String conteudo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom("analed988@gmail.com");
        mensagem.setTo("playy.story22@gmail.com");
        mensagem.setSubject("ALERTA DE SEGURANÇA - Usuária solicitando apoio");
        mensagem.setText(conteudo);

    }

    public void sendToken(String to, String token) {
        String url = "https://api.brevo.com/v3/smtp/email";

        System.out.println("apikey" + apiKey);
        System.out.println("email" + senderEmail);
        System.out.println("name" + senderName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey.trim());

        String htmlContent = "<p> Olá! \nSeu codigo de verificação para o app Dalia Calendario menstrual é: \n"
                + token +
                "\nEste codigo expira em 15 minutos.</p>";

        BrevoRequest payload = new BrevoRequest(
                new BrevoRequest.Sender(senderName, senderEmail),
                List.of(new BrevoRequest.To(to)),
                "Codigo de Verificação - Dalia",
                htmlContent
        );
        HttpEntity<BrevoRequest> request = new HttpEntity<>(payload, headers);

        try{
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("E-mail enviado com sucesso!");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Status HTTP Brevo: " + e.getStatusCode());
            System.err.println("Corpo da resposta Brevo: " + e.getResponseBodyAsString());

            throw new RuntimeException("Erro ao enviar e-mail: " + e.getResponseBodyAsString(), e);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro ao tentar enviar token do email" + e);
        }
    }
}