package com.dalia.ProjetoDalia.Services;

import com.dalia.ProjetoDalia.Model.Entity.SavedArticle;
import com.dalia.ProjetoDalia.Repository.SavedArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final SavedArticleRepository repository;

    public void saveBookmark(String userId, String articleId) {
        // Só salva se ainda não existir no banco para essa usuária
        if (!repository.existsByUserIdAndArticleId(userId, articleId)) {
            SavedArticle saved = new SavedArticle(userId, articleId);
            repository.save(saved);
        }
    }

    public void removeBookmark(String userId, String articleId) {
        repository.deleteByUserIdAndArticleId(userId, articleId);
    }
}