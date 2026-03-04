package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.thymeleaf.messageresolver.IMessageResolver;

public record VerificationDTO(
        @NotBlank(message = "O e-mail é obrigatorio")
        @Email
        String email,
        @NotBlank(message = "O codigo é obrigatorio")
        @Size(min = 6, max = 6, message = "O codigo deve ter 6 digitos")
        String token
) {
    public Users toEntity(){
        Users users = new Users();
        users.setEmail(this.email());
        users.setVerificationToken(this.token());
        return users;
    }
}
