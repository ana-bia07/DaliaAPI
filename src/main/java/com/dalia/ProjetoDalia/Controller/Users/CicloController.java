package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.SearchDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UserCycleDataDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Users.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/ciclo")
public class CicloController {

    private final SearchService searchService;

    public CicloController(SearchService searchService) {this.searchService = searchService;}

    @GetMapping("/status")
    public ResponseEntity<UserCycleDataDTO> getStatusHoje(){
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserCycleDataDTO status = searchService.getStatusCalendario(userLogado.getId());

        return ResponseEntity.ok(status);
    }

    @PutMapping("/registrar-menstruacao")
    public ResponseEntity<UserCycleDataDTO> registraMenstruacao(){
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserCycleDataDTO status = searchService.registrarCliqueBotao(userLogado.getId());

        return ResponseEntity.ok(status);
    }

}