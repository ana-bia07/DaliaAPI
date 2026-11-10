package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
        )
        String password,
        String passConfirmation
) {
    public Users toEntity(){
        Users users = new Users();
        users.setPassword(this.password());
        return users;
    }
}
