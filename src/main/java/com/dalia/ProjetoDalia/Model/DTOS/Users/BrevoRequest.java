package com.dalia.ProjetoDalia.Model.DTOS.Users;

import java.util.List;

public record BrevoRequest(
        Sender sender,
        List<To> to,
        String subject,
        String htmlContent
) {
    public record Sender(String name, String email){}
    public record To(String email){}
}
