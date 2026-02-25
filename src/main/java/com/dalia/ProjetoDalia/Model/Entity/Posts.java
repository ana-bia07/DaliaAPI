package com.dalia.ProjetoDalia.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    @Id
    private String id;

    private String idUsers;
    private String title;
    private String content;
    private int likes;
    private Instant createdAt;
    private List<Comments> comments = new ArrayList<>();
}
