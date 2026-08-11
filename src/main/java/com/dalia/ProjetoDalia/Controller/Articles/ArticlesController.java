package com.dalia.ProjetoDalia.Controller.Articles;

import com.dalia.ProjetoDalia.Model.DTOS.ArticlesDTO;
import com.dalia.ProjetoDalia.Model.Entity.Articles;
import com.dalia.ProjetoDalia.Model.Entity.Users.Users;
import com.dalia.ProjetoDalia.Services.ArticlesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Articles")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesController {

    private final ArticlesService ArticlesService;

    @GetMapping("/getTodos")
    public ResponseEntity<List<ArticlesDTO>> getAllArticles() {
        List<ArticlesDTO> ArticlesDTOs = ArticlesService.getAllArticles().stream()
                .map(ArticlesDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(ArticlesDTOs);
    }

    @PostMapping("/create")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ArticlesDTO> createArticles(@RequestBody ArticlesDTO ArticlesDTO) {
        Users userLogado = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Articles article = ArticlesDTO.toEntity();
        Articles savedArticle = ArticlesService.createArticles(article);
        ArticlesDTO responseDTO = ArticlesDTO.fromEntity(savedArticle);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("{idArticle}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<ArticlesDTO> updateArticles(@PathVariable String idArticle, @RequestBody ArticlesDTO articlesDTO) {
        Articles articles = articlesDTO.toEntity();
        return ArticlesService.updateArticles(idArticle, articles)
                .map(updateArticles -> ResponseEntity.ok(ArticlesDTO.fromEntity(updateArticles)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idArticle}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> deleteArticle(@PathVariable String idArticle) {
        ArticlesService.deleteArticles(idArticle);
        return ResponseEntity.noContent().build();
    }
}