package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.EventDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Event;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/pregnancy")
public class GravidezController {

    private final PregnancyMonitoringService pregnancyService;

    @PostMapping("/quiz")
    public ResponseEntity<PregnancyMonitoringDTO> savePregnancy(@RequestBody @Valid PregnancyMonitoringDTO pregnancydto) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PregnancyMonitoringDTO savedPregnancy = pregnancyService.saveOrUpdatePregnancy(userLogado.getId(), pregnancydto)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return ResponseEntity.ok(savedPregnancy);
    }

    @DeleteMapping("{idUser}")
    public void deletePregnancy(@PathVariable String idUser) {
        pregnancyService.deletePregnancy(idUser);
    }

    @PostMapping("/event")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EventDTO novoEvent = pregnancyService.createEvent(userLogado.getId(), eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvent);
    }

    @PutMapping("/event/{idEvent}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable String idEvent, @RequestBody EventDTO eventDTO) {
        return pregnancyService.updateEvent(idEvent, eventDTO)
        .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/event/{idEvent}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String idEvent) {
            pregnancyService.deleteEvent(idEvent);
            return ResponseEntity.noContent().build();
    }
}