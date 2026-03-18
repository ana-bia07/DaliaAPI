package com.dalia.ProjetoDalia.Model.DTOS.Posts;

import com.dalia.ProjetoDalia.Model.Entity.Comments;

import java.time.Instant;

public record CommentsDTO(
        String idUsers,
        String comment,
        Instant createdAt
){
    public Comments toEntity() {
        return new Comments(
                idUsers,
                comment,
                createdAt);

    }
}