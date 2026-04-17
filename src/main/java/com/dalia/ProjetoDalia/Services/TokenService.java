package com.dalia.ProjetoDalia.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dalia.ProjetoDalia.Model.DTOS.Users.LoginDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.LoginResponseDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private String secret = "5ff4dadba7a366051049e7b2e2ef7c8e37951710";

    private Integer expirationToken = 1;

    private Integer expirationRefresh = 2190;
    private final UsersRepository usersRepository;

    public LoginResponseDTO getTokens(Users user) {
        return new LoginResponseDTO(
                generateToken(user, expirationToken),
                generateToken(user, expirationRefresh)
        );
    }


    public String generateToken(Users user, Integer expiration) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate(expiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    public LoginResponseDTO getRefreshToken(String refreshToken) {
        var email = validateToken(refreshToken);
        var user = usersRepository.findByEmail(email).get();
         if(user == null){
             throw  new RuntimeException("Falhou a gerar um refresh token");
         }
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new LoginResponseDTO(
                generateToken(user, expirationToken),
                generateToken(user, expirationRefresh)
        );
    }

    private Instant genExpirationDate(Integer expiration) {
        return LocalDateTime.now().plusHours(expiration).toInstant(ZoneOffset.of("-03:00"));
    }
}
