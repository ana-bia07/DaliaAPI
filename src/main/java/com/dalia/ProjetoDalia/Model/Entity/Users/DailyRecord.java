package com.dalia.ProjetoDalia.Model.Entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "registro_diario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRecord {
    @Id
    private String id;
    private String idUser;
    private LocalDateTime date;
    private List<String> mood;
    private List<String> habits;
    private List<String> symptoms;
    private List<String> physical_activity;
    private List<String> sex;
    private List<String> discharge;
}
