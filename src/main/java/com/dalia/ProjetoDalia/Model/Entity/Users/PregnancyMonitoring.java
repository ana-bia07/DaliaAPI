package com.dalia.ProjetoDalia.Model.Entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "pregnant")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PregnancyMonitoring {
        private boolean isPregnant;
        private LocalDate startDate;
        private int gestationWeeks;
        @Field(name = "expectedBirthDate")
        private LocalDate expectedBirthDate;
        @Field(name = "dayPregnancy")
        private boolean plannedPregnancy;
        private boolean takeMedicine;
        private List<String> habits;
        private List<String> symptoms;
        private List<Event> event;
}
