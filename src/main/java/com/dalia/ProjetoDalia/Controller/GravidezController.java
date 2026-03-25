package com.dalia.ProjetoDalia.Controller;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.UsersDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import com.dalia.ProjetoDalia.Services.Users.UsersServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class GravidezController {

    private final PregnancyMonitoringService pregnancyService;

    @PostMapping("/searchPregnancy")
    public ResponseEntity<PregnancyMonitoringDTO> savePregnancy(@RequestBody @Valid PregnancyMonitoringDTO pregnancydto) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PregnancyMonitoringDTO savedPregnancy = pregnancyService.updatePregnancy(userLogado.getId(), pregnancydto)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return ResponseEntity.ok(savedPregnancy);
    }
}
