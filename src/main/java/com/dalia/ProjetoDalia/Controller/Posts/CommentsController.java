package com.dalia.ProjetoDalia.Controller.Posts;

import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Services.Posts.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addCommentToPost(@PathVariable String postId, @RequestBody Comments comment) {
        return commentsService.addComment(postId, comment)
                .map(post -> ResponseEntity.ok(post))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}