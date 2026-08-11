package com.dalia.ProjetoDalia.Model.DTOS.Users;

import com.dalia.ProjetoDalia.Model.Entity.Users.Event;
import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

public record PregnancyMonitoringDTO(
        boolean isPregnant,
        int gestationWeeks,
        LocalDate expectedBirthDate,
        boolean plannedPregnancy,
        boolean takeMedicine,
        List<String> habits,
        List<String> symptoms,
        List<Event> event
) {
    public PregnancyMonitoring toEntity() {
        return new PregnancyMonitoring(
            true,
            gestationWeeks,
            expectedBirthDate,
            plannedPregnancy,
            takeMedicine,
            habits,
            symptoms,
            event
        );
    }
    public static PregnancyMonitoringDTO fromEntity(PregnancyMonitoring entity) {
        return new PregnancyMonitoringDTO(
                entity.isPregnant(),
                entity.getGestationWeeks(),
                entity.getExpectedBirthDate(),
                entity.isPlannedPregnancy(),
                entity.isTakeMedicine(),
                entity.getHabits(),
                entity.getSymptoms(),
                entity.getEvent()
        );
    }
}
