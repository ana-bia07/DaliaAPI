package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsersDTO(
        String id,
        @NotBlank(message = "O nome é obrigatorio")
        String name,
        @NotBlank(message = "O sobrenome é obrigatorio")
        String surname,
        @Email(message = "O email não esta completo")
        @NotBlank(message = "O email é obrigatorio")
        String email,
        @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
        )
        @NotBlank(message = "A senha é obrigatoria")
        String password,
        boolean enable,
        Search search,
        PregnancyMonitoring pregnancyMonitoring
) {

    public UsersDTO() {
        this(null, "", "", "", "",false, null, null);
    }

    public UsersDTO(Users users){
        this(
                users.getId(),
                users.getName(),
                users.getSurname(),
                users.getEmail(),
                users.getPassword(),
                users.isEnable(),
                users.getSearch(),
                users.getPregnancyMonitoring()
        );
    }


    public Users toEntity(){
        Users user = new Users();
        user.setId(this.id());
        user.setName(this.name());
        user.setSurname(this.surname());
        user.setEmail(this.email());
        user.setPassword(this.password());
        user.setEnable(this.enable());
        user.setSearch(this.search());
        user.setPregnancyMonitoring(this.pregnancyMonitoring());
        return user;
    }
}
