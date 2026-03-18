package com.dalia.ProjetoDalia.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document (collection = "Report")
public class Report {
    @Id
    private String id;

    private String idUsers;
    private String title;
    private String description;
    private Instant reportDate;
}
