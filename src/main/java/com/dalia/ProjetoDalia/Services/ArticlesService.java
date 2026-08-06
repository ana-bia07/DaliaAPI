package com.dalia.ProjetoDalia.Services;

import com.dalia.ProjetoDalia.Model.Entity.Articles;
import com.dalia.ProjetoDalia.Model.Repository.ArticlesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ArticlesService {
    private final ArticlesRepository ArticlesRepository;

    public Articles createArticles(Articles Article) {
        return ArticlesRepository.save(Article);
    }

    public Optional<Articles> getArticleById(String idArticles) {
        return ArticlesRepository.findById(idArticles);
    }

    public List<Articles> getAllArticles() {
        return ArticlesRepository.findAll();
    }

    public Optional<Articles> updateArticles(String idArticles, Articles updatedArticle) {
        return ArticlesRepository.findById(idArticles).map(existingArticle -> {
            existingArticle.setTitle(updatedArticle.getTitle());
            existingArticle.setLink(updatedArticle.getLink());
            existingArticle.setCategory(updatedArticle.getCategory());
            return ArticlesRepository.save(existingArticle);
        });
    }

    public void deleteArticles(String idArticles) {
        ArticlesRepository.deleteById(idArticles);
    }
}
