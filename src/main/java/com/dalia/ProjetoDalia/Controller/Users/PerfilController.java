package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.SearchDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.EmailService;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import com.dalia.ProjetoDalia.Services.Users.SearchService;
import com.dalia.ProjetoDalia.Services.Users.UsersServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/perfil")
public class PerfilController {
    private final UsersRepository usersRepository;
    private final UsersServices usersServices;
    private EmailService emailService;
    private final SearchService searchService;
    private PregnancyMonitoringService pregnancyService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/perfilView")
    public ResponseEntity<?> getPerfil() {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("user", usersServices.getUserById(userLogado.getId()));
        response.put("search", searchService.getSearchByIdUsers(userLogado.getId()));

        return ResponseEntity.ok(response);
    }
    @PutMapping("/updatePerfil")
    public ResponseEntity<?> updatePerfil(@RequestBody @Valid UsersDTO userDTO) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String idUser = userLogado.getId();
        Optional<Users> userOriginalOpt = usersRepository.findById(idUser);
        if (userOriginalOpt.isPresent()) {
            Users user = userOriginalOpt.get();

            user.setName(userDTO.name());
            user.setSurname(userDTO.surname());
            user.setEmail(userDTO.email());
            if (userDTO.password() == null || userDTO.password().isBlank()) {
            } else {
                user.setPassword(passwordEncoder.encode(userDTO.password()));
            }
            if (user.getSearch() == null) {
                user.setSearch(new Search());
            }
            if (user.getPregnancyMonitoring() == null) {
                user.setPregnancyMonitoring(new PregnancyMonitoring());
            }

            user.getSearch().setAge(userDTO.search().getAge());
            user.getPregnancyMonitoring().setDayPregnancy(userDTO.pregnancyMonitoring().getDayPregnancy());
            user.getPregnancyMonitoring().setGestationWeeks(userDTO.pregnancyMonitoring().getGestationWeeks());
            user.getPregnancyMonitoring().setExpectedBirthDate(userDTO.pregnancyMonitoring().getExpectedBirthDate());

            usersRepository.save(user);

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("usuario não encontrado");
        }
    }


}