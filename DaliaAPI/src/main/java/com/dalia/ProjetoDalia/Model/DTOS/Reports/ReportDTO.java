package com.dalia.ProjetoDalia.Model.DTOS.Reports;

import com.dalia.ProjetoDalia.Model.Entity.Report;

import java.time.Instant;

public record ReportDTO(
        String idUsers,
        String title,
        String description,
        Instant reportDate
) {
    public Report toEntity() {
        return new Report(
                null,
                idUsers,
                title,
                description,
                reportDate);
    }
}
