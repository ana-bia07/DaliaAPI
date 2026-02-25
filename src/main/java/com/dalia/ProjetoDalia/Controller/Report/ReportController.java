package com.dalia.ProjetoDalia.Controller.Report;

import com.dalia.ProjetoDalia.Model.DTOS.Reports.ReportDTO;
import com.dalia.ProjetoDalia.Model.Entity.Report;
import com.dalia.ProjetoDalia.Services.Report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name = "Reports")
@RequestMapping("/reports")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

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


