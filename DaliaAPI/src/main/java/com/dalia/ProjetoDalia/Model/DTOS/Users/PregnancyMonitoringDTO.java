package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import java.time.LocalDate;
import java.util.List;

public record PregnancyMonitoringDTO(
        Boolean isPregnant,
        LocalDate dayPregnancy,
        int gestationWeeks,
        LocalDate expectedBirthDate,
        List<String> consultations
) {
    public PregnancyMonitoring toEntity() {
        return new PregnancyMonitoring(
                isPregnant,
                dayPregnancy,
                gestationWeeks,
                expectedBirthDate,
                consultations
        );
    }

    // Construtor padrão opcional (para inicialização vazia)
    public PregnancyMonitoringDTO() {
        this(false, null, 0, null, List.of());
    }
}
