package com.dalia.ProjetoDalia.Model.DTOS.Users;

import java.time.LocalDate;

public record UserCycleDataDTO (
        int minCycleDuration,
        int maxCycleDuration,
        LocalDate lastMenstruationDay,
        boolean isMenstruando,
        boolean isPeriodoFertil,
        boolean isOvulacao,
        long diasDeAtraso,
        LocalDate inicioMenstruacao,
        LocalDate fimMenstruacao,
        LocalDate inicioPeriodoFertil,
        LocalDate fimPeriodoFertil,
        LocalDate diaOvulacao
) {

}