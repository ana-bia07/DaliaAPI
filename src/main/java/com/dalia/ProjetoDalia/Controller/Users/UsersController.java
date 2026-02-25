package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.SearchDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Usuários")
@RestController
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
/*
    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("users", new UsersDTO(null, null, null, null, null, null, null));
        return "cadastro";
    }

*/
    @PostMapping("/criarUsuario")
    public ResponseEntity<?> createUserForm(@Valid @RequestBody UsersDTO user, @RequestParam String passconfirmation)
    {
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

    @GetMapping("/login")
    public String MostraLogin(){
        return "Login";
    }

    @PostMapping("/RealizaLogin")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession Session) {
        Optional<UsersDTO> optionalUser = usersService.getByEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get().toEntity();
            if (user.getPassword().equals(password)) {
                Session.setAttribute("idUser", user.getId());

                if (user.getSearch() != null) {
                    Session.setAttribute("search", user.getSearch());
                }
                model.addAttribute("user", user);
                return "redirect:/home";
            }
        }
        model.addAttribute("error", "E-mail ou senha inválidos!");
        return "Login";
    }

    @GetMapping("/search")
    public String mostrarFormulario(Model model,HttpSession session) {
        String idUser = (String) session.getAttribute("idUser");

        if(idUser == null) {
            return "redirect:/cadastro";
        }

        model.addAttribute("idUser", idUser);
        model.addAttribute("search", new Search());
        return "perguntas";
    }

    @PostMapping("/salvar-respostas")
    public String processarFormulario(@ModelAttribute("search") SearchDTO search, HttpSession session) {
        String idUser = (String) session.getAttribute("idUser");
        if (idUser == null) {
            return "redirect:/login";
        }

        searchService.saveOrUpdateSearchForUser(idUser, search);
        session.setAttribute("search", search.toEntity());

        return "redirect:/Mhome";
    }
}