package com.dalia.ProjetoDalia.Services.Report;

import com.dalia.ProjetoDalia.Model.DTOS.Reports.ReportDTO;
import com.dalia.ProjetoDalia.Model.Entity.Report;
import com.dalia.ProjetoDalia.Model.Repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository ReportRepository;

    public Report createReport(ReportDTO reportDTO) {
        Report Report = reportDTO.toEntity();
        return ReportRepository.save(Report);
    }

    public Optional<Report> getReportById(String id) {
        return ReportRepository.findById(id);
    }

    public Optional<Report> updateReport(String id, ReportDTO reportDTO) {
        return ReportRepository.findById(id).map(Report -> {
            Report.setDescription(reportDTO.description());
            return ReportRepository.save(Report);
        });
    }

    public void deleteReport(String id) {
        ReportRepository.deleteById(id);
    }

    public List<Report> getReportsByUsers(String idUsers) {
        return ReportRepository.findByIdUsers(idUsers);
    }

}

