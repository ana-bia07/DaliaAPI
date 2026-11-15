package com.dalia.ProjetoDalia.Repository;

import com.dalia.ProjetoDalia.Model.Entity.SavedArticle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedArticleRepository extends MongoRepository<SavedArticle, String> {

    // Verifica se já está salvo (evita duplicatas)
    boolean existsByUserIdAndArticleId(String userId, String articleId);
    void deleteByUserIdAndArticleId(String userId, String articleId);
}