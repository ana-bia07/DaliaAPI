package com.dalia.ProjetoDalia.Controller.Users;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.dalia.ProjetoDalia.Model.DTOS.Users.*;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.TokenService;
import com.dalia.ProjetoDalia.Services.Users.SearchService;
import com.dalia.ProjetoDalia.Services.Users.UsersServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Tag(name = "Usuários")
@RestController
@RequestMapping("/api/user")
public class UsersController {

    private final UsersServices usersService;
    private final SearchService searchService;
    private final TokenService tokenService;

    public UsersController(UsersServices usersServices, SearchService searchService, TokenService tokenService) {
        this.usersService = usersServices;
        this.searchService = searchService;
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    public String redirectToLandingPage() {
        return "landingP";
    }

    //cria o conta
    @PostMapping("/criarUsuario")
    public ResponseEntity<?> createUserForm(@Valid @RequestBody UsersDTO user) {
        if (!user.password().equals(user.passConfirmation())) {
            return ResponseEntity.badRequest().body("As senhas não coincidem.");
        }
        try {
            UsersDTO newUser = usersService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //verifica email
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody @Valid VerificationDTO verificationDTO) {
        try{
            LoginResponseDTO result = usersService.verifyEmail(verificationDTO);
            return ResponseEntity.ok(result);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //cria o token de navegação
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            LoginResponseDTO respose = usersService.login(loginDTO);
            return ResponseEntity.ok(respose);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RequestRTokenDTO refreshTokendto) {
        try{
            LoginResponseDTO response = tokenService.getRefreshToken(refreshTokendto.refreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/search")
    public ResponseEntity<SearchDTO> saveSearch(@RequestBody @Valid SearchDTO searchdto) {
       Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       SearchDTO savedSearch = searchService.saveOrUpdateSearchForUser(userLogado.getId(), searchdto)
               .orElseThrow(() -> new RuntimeException("Usuário não encontrado para salvar pesquisa"));
       return ResponseEntity.ok(savedSearch);
    }

    @DeleteMapping("/{idUser}/delete")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteUser(@PathVariable String idUser) {
        usersService.deleteUser(idUser);
        return  ResponseEntity.noContent().build();
    }
}