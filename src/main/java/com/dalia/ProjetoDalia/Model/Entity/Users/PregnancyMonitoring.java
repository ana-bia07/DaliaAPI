package com.dalia.ProjetoDalia.Model.Entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "pregnant")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PregnancyMonitoring {

    private Boolean isPregnant;
    @Field(name = "dayPregnancy")
    private LocalDate dayPregnancy;
    private int gestationWeeks;
    @Field(name = "expectedBirthDate")
    private LocalDate expectedBirthDate;
    private List<String> consultations;
}
