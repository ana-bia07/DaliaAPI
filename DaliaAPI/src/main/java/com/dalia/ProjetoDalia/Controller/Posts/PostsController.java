package com.dalia.ProjetoDalia.Controller.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.PostsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Services.Posts.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@Tag(name = "Posts")
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping
    public String forumPage(Model model) {
        List<PostsDTO> postsDTOs = postsService.getAllPosts().stream()
                .map(PostsDTO::fromEntity)
                .toList();

        model.addAttribute("posts", postsDTOs);
        model.addAttribute("newPost", new PostsDTO(
                null, "", "", "", 0, Instant.now(), List.of()
        ));
        return "forum";
    }

    @PostMapping("/create-form")
    public String createPostFromForm(@ModelAttribute("newPost") PostsDTO postsDTO) {
        Posts post = postsDTO.toEntity();
        post.setCreatedAt(Instant.now());
        post.setLikes(0);
        postsService.createPost(post);
        return "redirect:/posts";
    }

    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<List<PostsDTO>> getAllPosts() {
        List<PostsDTO> postsDTOs = postsService.getAllPosts().stream()
                .map(PostsDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(postsDTOs);
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<PostsDTO> createPost(@RequestBody PostsDTO postsDTO) {
        Posts postParaCriar = postsDTO.toEntity();
        postParaCriar.setCreatedAt(Instant.now());
        postParaCriar.setLikes(0);
        Posts createdPost = postsService.createPost(postParaCriar);

        return ResponseEntity.created(URI.create("/forum/" + createdPost.getId()))
                .body(PostsDTO.fromEntity(createdPost));
    }

    @ResponseBody
    @GetMapping("/{idPosts}")
    public ResponseEntity<PostsDTO> getPost(@PathVariable String idPosts) {
        return postsService.getPostById(idPosts)
                .map(PostsDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseBody
    @PutMapping("/{idPosts}")
    public ResponseEntity<PostsDTO> updatePost(@PathVariable String idPosts, @RequestBody PostsDTO postsDTO) {
        return postsService.updatePost(idPosts, postsDTO.toEntity())
                .map(PostsDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseBody
    @DeleteMapping("/{idPosts}")
    public ResponseEntity<Void> deletePost(@PathVariable String idPosts) {
        postsService.deletePost(idPosts);
        return ResponseEntity.noContent().build();
    }


    @ResponseBody
    @PutMapping("/{idPosts}/like")
    public ResponseEntity<Void> addLike(@PathVariable String idPosts) {
        boolean success = postsService.incrementLikes(idPosts);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @ResponseBody
    @PutMapping("/{idPosts}/unlike")
    public ResponseEntity<Void> removeLike(@PathVariable String idPosts) {
        boolean success = postsService.decrementLikes(idPosts);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}