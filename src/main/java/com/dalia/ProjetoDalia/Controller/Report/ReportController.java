package com.dalia.ProjetoDalia.Controller.Report;

import com.dalia.ProjetoDalia.Model.DTOS.Reports.ReportDTO;
import com.dalia.ProjetoDalia.Model.Entity.Report;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.EmailService;
import com.dalia.ProjetoDalia.Services.Report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Reports")
@RequestMapping("/api/reports")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final EmailService emailService;

    @PostMapping("/enviar-denuncia")
    public ResponseEntity<?> enviarDenuncia(@RequestBody String mensagem) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String corpo = "ALERTA: A usuária " + userLogado.getName() + " enviou um relato de abuso.\n" +
                "E-mail de contato: " + userLogado.getEmail() + "\n" +
                "Data/Hora: " + LocalDateTime.now() + "\n\n" +
                "RELATO DA USUÁRIA:\n" + mensagem;

        emailService.enviarDenuncia(corpo);

        // 4. Retornamos uma mensagem de acolhimento para o Android
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Seu relato foi recebido com total sigilo. Você não está sozinha.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{idUsers}")
    public ResponseEntity<Report> createReport(@PathVariable String idUsers, @RequestBody ReportDTO reportDTO) {
        reportDTO = new ReportDTO(idUsers, reportDTO.title(), reportDTO.description(), reportDTO.reportDate()); // Set idUsers
        Report createdReport = reportService.createReport(reportDTO);
        return ResponseEntity.created(URI.create("/reports/" + createdReport.getId())).body(createdReport);
    }

    @GetMapping("/{idReport}")
    public ResponseEntity<Report> getReportById(@PathVariable String idReport) {
        Optional<Report> report = reportService.getReportById(idReport);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{idReport}")
    public ResponseEntity<Report> updateReport(@PathVariable String idReport, @RequestBody ReportDTO reportDTO) {
        Optional<Report> updatedReport = reportService.updateReport(idReport, reportDTO);
        return updatedReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idReport}")
    public ResponseEntity<Void> deleteReport(@PathVariable String idReport) {
        reportService.deleteReport(idReport);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{idUsers}")
    public ResponseEntity<List<Report>> getReportsByidUsers(@PathVariable String idUsers) {
        var reports = reportService.getReportsByUsers(idUsers);
        return ResponseEntity.ok(reports);
    }

}


