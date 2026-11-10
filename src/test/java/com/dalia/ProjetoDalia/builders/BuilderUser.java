package com.dalia.ProjetoDalia.builders;

import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;

public class BuilderUser {

    private String id = null;
    private String name = "Ana";
    private String surname = "Silva";
    private String email = "ana.teste@dalia.com";
    private String password = "SenhaSegura123!";
    private String confirmPassword = "SenhaSegura123!";
    private String modo = "MENSTRUACAO";

    private BuilderUser() {}

    public static BuilderUser umUsuario() {
        return new BuilderUser();
    }

    public BuilderUser comEmail(String email) {
        this.email = email;
        return this;
    }

    public BuilderUser comNome(String name) {
        this.name = name;
        return this;
    }

    public UsersDTO buildDTO() {
        // Construtor compatível com o seu record/classe UsersDTO
        return new UsersDTO(
                id,
                name,
                surname,
                email,
                password,
                confirmPassword,
                modo,
                false,
                null, // search
                null  // pregnancyMonitoring
        );
    }
}