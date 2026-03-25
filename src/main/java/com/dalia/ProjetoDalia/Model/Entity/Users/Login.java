package com.dalia.ProjetoDalia.Model.Entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private String id;
    private String email;
    private String password;
}
