package com.dalia.ProjetoDalia.Model.Entity.Users;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "search")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Search {

    private int age;
    private boolean regularMenstruation;
    private boolean useContraceptive;
    private String contraceptiveType;
    @Field(name = "lastMenstruationDay")
    private LocalDate lastMenstruationDay;
    private Integer menstruationDuration = 5;
    private int cycleDuration;
    private List<Integer> cycleHistory = new ArrayList<>();
    private Integer minCycleDuration;
    private Integer maxCycleDuration;
}