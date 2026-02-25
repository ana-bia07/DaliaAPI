package com.dalia.ProjetoDalia.Controller.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Services.Users.PregnancyMonitoringService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Pregnancy")
@RestController
@RequestMapping("/pregnancy")
@RequiredArgsConstructor
public class PregnancyMonitoringController {

    private final PregnancyMonitoringService service;

    @PostMapping("/{idUsers}")
    public ResponseEntity<String> create(@PathVariable String idUsers, @RequestBody PregnancyMonitoringDTO dto) {
        String result = service.createPregnancyMonitoring(idUsers, dto);
        return ResponseEntity.created(URI.create("/pregnancy/" + idUsers)).body(result);
    }

    @GetMapping("/{idUsers}")
    public ResponseEntity<PregnancyMonitoringDTO> get(@PathVariable String idUsers) {
        return service.getPregnancyByIdUser(idUsers)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{idUsers}")
    public ResponseEntity<PregnancyMonitoringDTO> update(@PathVariable String idUsers, @RequestBody PregnancyMonitoringDTO dto) {
        return service.updatePregnancy(idUsers, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idUsers}")
    public ResponseEntity<Void> delete(@PathVariable String idUsers) {
        service.deletePregnancy(idUsers);
        return ResponseEntity.noContent().build();
    }
}
