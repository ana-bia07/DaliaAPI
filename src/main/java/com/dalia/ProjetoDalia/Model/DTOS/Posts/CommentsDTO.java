package com.dalia.ProjetoDalia.Model.DTOS.Posts;

import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Posts;

import java.time.Instant;

public record CommentsDTO(
        String id,
        String idUsers,
        String comment,
        Instant createdAt
){
    public Comments toEntity() {
        return new Comments(
                id,
                "user dalia",
                comment,
                createdAt);
    }
    public static CommentsDTO fromEntity(Comments comments) {
        return new CommentsDTO(
                comments.getId(),
                "user Dalia",
                comments.getComment(),
                comments.getCreatedAt()
        );
    }
}