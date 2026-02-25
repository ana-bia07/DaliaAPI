package com.dalia.ProjetoDalia.Model.Entity.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private Search search;
    private PregnancyMonitoring pregnancyMonitoring;
}
