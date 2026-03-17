package com.dalia.ProjetoDalia.Model.Entity.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {

    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private boolean enable = false;
    private String verificationToken;
    private LocalDateTime tokenExpirantion;
    private Search search;
    private PregnancyMonitoring pregnancyMonitoring;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Define que todo usuário cadastrado tem o perfil "USER"
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getId(){return id;}

    @Override
    public String getUsername() {
        return email; // O e-mail será o seu login
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable; // Usa o campo que você já tem no banco!
    }
}