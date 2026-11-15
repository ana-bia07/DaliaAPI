package com.dalia.ProjetoDalia.Model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "saved_articles")
@Getter
@Setter
@NoArgsConstructor
public class SavedArticle {

    @Id
    private String id;
    private String userId;
    private String articleId;
    private Instant createdAt;

    public SavedArticle(String userId, String articleId) {
        this.userId = userId;
        this.articleId = articleId;
        this.createdAt = Instant.now();
    }
}