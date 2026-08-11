package com.dalia.ProjetoDalia.Services.Interface;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;

import java.util.Optional;

public interface IPregnancyMonitoringService {
    public Optional<PregnancyMonitoringDTO> saveOrUpdatePregnancy(String idUser, PregnancyMonitoringDTO pregnancyDTO);
    public Optional<PregnancyMonitoringDTO> getPregnancyByIdUser(String idUser);
    public void deletePregnancy(String idUser);
}
