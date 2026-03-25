package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.*;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Users.SearchService;
import com.dalia.ProjetoDalia.Services.Users.UsersServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api")
public class UsersController {

    private final UsersServices usersService;
    private final SearchService searchService;

    public UsersController(UsersServices usersServices, SearchService searchService) {
        this.usersService = usersServices;
        this.searchService = searchService;

    }

    @GetMapping("/")
    public String redirectToLandingPage() {
        return "landingP";
    }

    //cria o conta
    @PostMapping("/criarUsuario")
    public ResponseEntity<?> createUserForm(@Valid @RequestBody UsersDTO user, @RequestParam String passconfirmation) {
        if (!user.password().equals(passconfirmation)) {
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
    public ResponseEntity<String> verify(@RequestBody @Valid VerificationDTO verificationDTO) {
        String result = usersService.verifyEmail(verificationDTO);

        if(result.contains("sucesso")){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    //cria o token de navegação
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        String token = usersService.login(loginDTO);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @PostMapping("/search")
    public ResponseEntity<SearchDTO> saveSearch(@RequestBody @Valid SearchDTO searchdto) {
       Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       SearchDTO savedSearch = searchService.saveOrUpdateSearchForUser(userLogado.getId(), searchdto)
               .orElseThrow(() -> new RuntimeException("Usuário não encontrado para salvar pesquisa"));
       return ResponseEntity.ok(savedSearch);
    }
}