package com.dalia.ProjetoDalia.Model.Entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Consultations {
    @Field(name = "date")
    private Instant date;
    private String observations;
}
