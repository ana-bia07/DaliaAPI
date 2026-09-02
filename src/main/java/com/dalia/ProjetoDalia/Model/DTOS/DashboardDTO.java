package com.dalia.ProjetoDalia.Model.DTOS;

import com.dalia.ProjetoDalia.Model.Entity.Report;

import java.util.List;
import java.util.Map;

public record DashboardDTO(
        int modoMenstruacao,
        int modoGravidez,
        List<Report> denuncias,
        Map<String, Integer> categoriaPost
        ){
    public static DashboardDTO fromEntity(DashboardDTO dashboard){
        return new DashboardDTO(
                dashboard.modoMenstruacao(),
                dashboard.modoGravidez(),
                dashboard.denuncias(),
                dashboard.categoriaPost()
        );
    }
}
