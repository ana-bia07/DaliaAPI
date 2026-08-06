package com.dalia.ProjetoDalia.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Articles {
    @Id
    private String id;
    private String title;
    private String link;
    private String category;
    //private String image;
}
