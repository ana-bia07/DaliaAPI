package com.dalia.ProjetoDalia.Controller.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.PostsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Posts.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@Tag(name = "Posts")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping
    public ResponseEntity<List<PostsDTO>> getAllPosts() {
        List<PostsDTO> postsDTOs = postsService.getAllPosts().stream()
                .map(PostsDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(postsDTOs);
    }

    @PostMapping("/create-form")
    public ResponseEntity<PostsDTO> createPost(@RequestBody PostsDTO postsDTO) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Posts post = postsDTO.toEntity();
        post.setIdUsers(userLogado.getId());
        post.setCreatedAt(Instant.now());
        post.setLikes(0);
        Posts savedPost = postsService.createPost(post);
        PostsDTO responseDTO = PostsDTO.fromEntity(savedPost);

        return ResponseEntity.ok(responseDTO);
    }

        // 3. CURTIR UM POST (Lógica de Like)
    @PutMapping("/{idPosts}/like")
    public ResponseEntity<Void> addLike(@PathVariable String idPosts) {
        boolean success = postsService.incrementLikes(idPosts);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

        // 4. REMOVER LIKE (Unlike)
    @PutMapping("/{idPosts}/unlike")
    public ResponseEntity<Void> removeLike(@PathVariable String idPosts) {
        boolean success = postsService.decrementLikes(idPosts);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
     // ... os outros métodos (get, update, delete) seguem o mesmo padrão
}