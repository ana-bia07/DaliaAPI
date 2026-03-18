package com.dalia.ProjetoDalia.Services.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Interface.IPregnancyMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PregnancyMonitoringService implements IPregnancyMonitoringService {

    private final UsersRepository usersRepository;

    @Override
    public String createPregnancyMonitoring(String idUser, PregnancyMonitoringDTO dto) {
        Optional<Users> userOpt = usersRepository.findById(idUser);
        if (userOpt.isEmpty()) return "Usuário não encontrado";

        Users user = userOpt.get();
        PregnancyMonitoring pregnancyMonitoring = dto.toEntity();

        user.setPregnancyMonitoring(pregnancyMonitoring);
        usersRepository.save(user);

        return "Gravidez registrada com sucesso";
    }

    @Override
    public Optional<PregnancyMonitoringDTO> getPregnancyByIdUser(String idUser) {
        return usersRepository.findById(idUser)
                .map(Users::getPregnancyMonitoring)
                .map(pregnancyMonitoring -> new PregnancyMonitoringDTO(
                        pregnancyMonitoring.getIsPregnant(),
                        pregnancyMonitoring.getDayPregnancy(),
                        pregnancyMonitoring.getGestationWeeks(),
                        pregnancyMonitoring.getExpectedBirthDate(),
                        pregnancyMonitoring.getConsultations()
                ));
    }

    @Override
    public Optional<PregnancyMonitoringDTO> updatePregnancy(String idUsers, PregnancyMonitoringDTO dto) {
        Optional<Users> userOpt = usersRepository.findById(idUsers);
        if (userOpt.isEmpty()) return Optional.empty();

        Users user = userOpt.get();
        user.setPregnancyMonitoring(dto.toEntity());
        usersRepository.save(user);

        return Optional.of(dto);
    }
    @Override
    public void deletePregnancy(String idUser) {
        Optional<Users> userOpt = usersRepository.findById(idUser);
        userOpt.ifPresent(user -> {
            user.setPregnancyMonitoring(null);
            usersRepository.save(user);
        });
    }
}
