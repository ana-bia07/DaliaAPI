package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/pregnancy")
public class GravidezController {

    private final PregnancyMonitoringService pregnancyService;

    @PostMapping("/search")
    public ResponseEntity<PregnancyMonitoringDTO> savePregnancy(@RequestBody @Valid PregnancyMonitoringDTO pregnancydto) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PregnancyMonitoringDTO savedPregnancy = pregnancyService.updatePregnancy(userLogado.getId(), pregnancydto)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return ResponseEntity.ok(savedPregnancy);
    }
}
