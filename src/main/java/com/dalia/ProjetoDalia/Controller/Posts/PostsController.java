package com.dalia.ProjetoDalia.Controller.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.PostsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.Posts.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "Posts")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;


    @GetMapping("/getTodos")
    public ResponseEntity<List<PostsDTO>> getPosts(@RequestParam(required = false) String category) {
        if(StringUtils.hasText(category)){
            return ResponseEntity.ok(postsService.searchCategory(category));
        }
        return ResponseEntity.ok(postsService.getAllPosts());
    }

    @PostMapping("/create")
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

        // 3. CURTIR UM POST
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

    @DeleteMapping("/{idPost}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deletePost(@PathVariable String idPost) {
        postsService.deletePost(idPost);
        return ResponseEntity.noContent().build();
    }
}