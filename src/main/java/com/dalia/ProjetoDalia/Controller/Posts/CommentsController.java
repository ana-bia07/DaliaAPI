package com.dalia.ProjetoDalia.Controller.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.CommentsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Posts.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/{postId}/add")
    public ResponseEntity<CommentsDTO> addComment(
            @PathVariable String postId,
            @RequestBody CommentsDTO commentsDTO
    ) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comments novoComentario = commentsDTO.toEntity();

        novoComentario.setIdUsers(userLogado.getId()); // Dono do comentário
        novoComentario.setCreatedAt(Instant.now()); // Nome anônimo

        Optional<Comments> savedComment = commentsService.addComment(postId, CommentsDTO.fromEntity(novoComentario));

        return ResponseEntity.ok(CommentsDTO.fromEntity(savedComment.orElse(null)));
    }
}