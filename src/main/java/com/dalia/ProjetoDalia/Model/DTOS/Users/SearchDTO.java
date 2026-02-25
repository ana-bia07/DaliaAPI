package com.dalia.ProjetoDalia.Model.DTOS.Users;
import com.dalia.ProjetoDalia.Model.Entity.Users.Search;
import java.time.LocalDate;
import java.util.List;

public record SearchDTO(
        int age,
        boolean regularMenstruation,
        boolean useContraceptive,
        String contraceptiveType,
        LocalDate lastMenstruationDay,
        int cycleDuration,
        Integer menstruationDuration,
        List<Integer> cycleHistory,
        Integer minCycleDuration,
        Integer maxCycleDuration
) {
    public Search toEntity() {
        return new Search(
                age,
                regularMenstruation,
                useContraceptive,
                contraceptiveType,
                lastMenstruationDay,
                menstruationDuration != null ? menstruationDuration : 5,  // valor padrão
                cycleDuration,
                null,
                minCycleDuration,
                maxCycleDuration
        );
    }
}
