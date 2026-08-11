package com.dalia.ProjetoDalia.Model.DTOS.Users;

import ch.qos.logback.core.model.EventEvaluatorModel;
import com.dalia.ProjetoDalia.Model.Entity.Users.Event;

import java.time.LocalDateTime;

public record EventDTO (
    String idUsers,
    String titulo,
    String descricao,
    LocalDateTime dataHora,
    String local
){
    public Event toEntity() {
      return new Event(
              null,
              idUsers,
              titulo,
              descricao,
              dataHora,
              local
      );
  }
    public static EventDTO fromEntity(Event evento) {
        return new EventDTO(
                evento.getIdUsers(),
                evento.getTitulo(),
                evento.getDescricao(),
                evento.getDataHora(),
                evento.getLocal()
        );
    }
}
