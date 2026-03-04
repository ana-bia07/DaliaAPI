package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "O e-mail é obrigatorio")
        @Email(message = "E-mail invalido")
        String email,
        @NotBlank(message = "A senha é obrigatoria")
        String passsord
) {
    public Users toEntity(){
        Users user = new Users();
        user.setEmail(this.email());
        user.setPassword(this.passsord());
        return user;
    }
}
