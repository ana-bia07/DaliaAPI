package com.dalia.ProjetoDalia.Model.DTOS.Posts;

import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Posts;

import java.time.Instant;
import java.util.List;

public record PostsDTO(
        String id,
        String idUsers,
        String title,
        String content,
        Integer likes,
        Instant createdAt,
        List<Comments> comments
) {
    public Posts toEntity() {
        int likesValue = (likes != null) ? likes : 0;
        return new Posts(
                id,
                idUsers,
                title,
                content,
                likesValue,
                createdAt,
                comments
        );
    }

    public static PostsDTO fromEntity(Posts post) {
        return new PostsDTO(
                post.getId(),
                post.getIdUsers(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                post.getCreatedAt(),
                post.getComments()
        );
    }
}