package com.dalia.ProjetoDalia.Model.DTOS;
import com.dalia.ProjetoDalia.Model.Entity.Articles;


public record ArticlesDTO(
        String id,
        String title,
        String link,
        String category
        //private String image;
) {
    public Articles toEntity() {
        return new Articles(
                id,
                title,
                link,
                category
        );
    }
    public static ArticlesDTO fromEntity(Articles article) {
        return new ArticlesDTO(
                article.getId(),
                article.getTitle(),
                article.getLink(),
                article.getCategory()
        );
    }
}
