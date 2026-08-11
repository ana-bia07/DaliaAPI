package com.dalia.ProjetoDalia.Services.Users;

import com.dalia.ProjetoDalia.Model.DTOS.Users.EventDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.PregnancyMonitoringDTO;
import com.dalia.ProjetoDalia.Model.DTOS.Users.SemanaDTO;
import com.dalia.ProjetoDalia.Model.Entity.Users.Event;
import com.dalia.ProjetoDalia.Model.Entity.Users.PregnancyMonitoring;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.Resource;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Model.Repository.EventRespository;
import com.dalia.ProjetoDalia.Model.Repository.UsersRepository;
import com.dalia.ProjetoDalia.Services.Interface.IPregnancyMonitoringService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;


import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PregnancyMonitoringService implements IPregnancyMonitoringService {

    private final UsersRepository usersRepository;
    private final EventRespository eventRespository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private List<SemanaDTO> semanaslist;

    @PostConstruct
    public void init() {
        try{
            Resource resource = resourceLoader.getResource("classpath:gestacao_semanas.json");
            InputStream inputStream = resource.getInputStream();
            this.semanaslist = objectMapper.readValue(inputStream, new TypeReference<List<SemanaDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException("erro ao carregar arquivo json");
        }
    }

    @Override
    public Optional<PregnancyMonitoringDTO> getPregnancyByIdUser(String idUser) {
        return usersRepository.findById(idUser)
                .map(Users::getPregnancyMonitoring)
                .map(pregnancy -> new PregnancyMonitoringDTO(
                        pregnancy.isPregnant(),
                        pregnancy.getGestationWeeks(),
                        pregnancy.getExpectedBirthDate(),
                        pregnancy.isPlannedPregnancy(),
                        pregnancy.isTakeMedicine(),
                        pregnancy.getHabits(),
                        pregnancy.getSymptoms(),
                        pregnancy.getEvent()
                ));
    }

    @Override
    public Optional<PregnancyMonitoringDTO> saveOrUpdatePregnancy(String idUsers, PregnancyMonitoringDTO dto) {
        Optional<Users> userOpt = usersRepository.findById(idUsers);
        if (userOpt.isEmpty()) return Optional.empty();

        Users user = userOpt.get();
        PregnancyMonitoring pregnancy = user.getPregnancyMonitoring();
        if(pregnancy == null) {
            pregnancy = dto.toEntity();
        } else {
            pregnancy.setPregnant(dto.isPregnant());
            pregnancy.setGestationWeeks(dto.gestationWeeks());
            pregnancy.setExpectedBirthDate(dto.expectedBirthDate());
            pregnancy.setPlannedPregnancy(dto.plannedPregnancy());
            pregnancy.setHabits(dto.habits());
            pregnancy.setSymptoms(dto.symptoms());
            pregnancy.setEvent(dto.event());
        }

        user.setPregnancyMonitoring(pregnancy);

        Users savedUser = usersRepository.save(user);

        PregnancyMonitoring savedPregnancy = savedUser.getPregnancyMonitoring();

        return Optional.of(new PregnancyMonitoringDTO(
                savedPregnancy.isPregnant(),
                savedPregnancy.getGestationWeeks(),
                savedPregnancy.getExpectedBirthDate(),
                savedPregnancy.isPlannedPregnancy(),
                savedPregnancy.isTakeMedicine(),
                savedPregnancy.getHabits(),
                savedPregnancy.getSymptoms(),
                savedPregnancy.getEvent()
        ));
    }

    @Override
    public void deletePregnancy(String idUser) {
        Optional<Users> userOpt = usersRepository.findById(idUser);
        userOpt.ifPresent(user -> {
            user.setPregnancyMonitoring(null);
            usersRepository.save(user);
        });
    }

    public EventDTO createEvent(String idUser, EventDTO dto) {
        Event event = dto.toEntity();
        event.setIdUsers(idUser);
        Event salvarEvento = eventRespository.save(event);
        return EventDTO.fromEntity(salvarEvento);
    }

    public List<EventDTO> getEventByIdUser(String idUser) {
        return eventRespository.findById(idUser)
                .stream().map(EventDTO::fromEntity)
                .toList();
    }

    public Optional<EventDTO> updateEvent(String idEvent, EventDTO dto) {
        return eventRespository.findById(idEvent).map(existingEvent -> {
            if (StringUtils.hasText(dto.titulo())) existingEvent.setTitulo(dto.titulo());
            if (StringUtils.hasText(dto.descricao())) existingEvent.setDescricao(dto.descricao());
            if (dto.dataHora() != null) existingEvent.setDataHora(dto.dataHora());
            if (StringUtils.hasText(dto.local())) existingEvent.setLocal(dto.local());;
            Event updatedEvent = eventRespository.save(existingEvent);
            return EventDTO.fromEntity(updatedEvent);
        });
    }

    public SemanaDTO getSemana(int indexSemana){
        return semanaslist.stream()
                .filter(info -> info.semana() == indexSemana)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Informações da semana" + indexSemana + " não encontrada"));
    }


    public void deleteEvent(String idEvent) {
        eventRespository.deleteById(idEvent);
    }
}
