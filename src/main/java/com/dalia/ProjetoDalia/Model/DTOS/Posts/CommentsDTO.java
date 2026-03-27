package com.dalia.ProjetoDalia.Model.DTOS.Posts;

import com.dalia.ProjetoDalia.Model.Entity.Comments;

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
    public static CommentsDTO fromEntity(Comments comments, String id) {
        return new CommentsDTO(
                comments.getId(),
                "user Dalia",
                comments.getComment(),
                comments.getCreatedAt()
        );
    }
}