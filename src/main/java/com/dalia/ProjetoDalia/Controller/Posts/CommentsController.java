package com.dalia.ProjetoDalia.Controller.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.CommentsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Posts.CommentsService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/{postId}/addComment")
    public ResponseEntity<CommentsDTO> addComment(@PathVariable String postId, @RequestBody CommentsDTO commentsDTO)
    {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comments novoComentario = commentsDTO.toEntity();

        novoComentario.setIdUsers(userLogado.getId());
        novoComentario.setCreatedAt(Instant.now());

        return commentsService.addComment(postId, novoComentario)
                .map(savedEntity -> ResponseEntity.ok(CommentsDTO.fromEntity(savedEntity, userLogado.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idPost}/{indexComment}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void>  deleteComment(@PathVariable String idPost, @PathVariable int indexComment) {
        commentsService.deleteComments(idPost, indexComment);
        return ResponseEntity.noContent().build();
    }
}