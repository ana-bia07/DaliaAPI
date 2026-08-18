package com.dalia.ProjetoDalia.Services.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.PostsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Model.Repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    public Posts createPost(Posts post) {
        return postsRepository.save(post);
    }

    public Optional<Posts> getPostById(String idPosts) {
        return postsRepository.findById(idPosts);
    }

    public List<PostsDTO> getAllPosts() {
        return postsRepository.findAll()
                .stream().map(PostsDTO::fromEntity).toList();
    }

    public Optional<Posts> updatePost(String idPosts, Posts updatedPost) {
        return postsRepository.findById(idPosts).map(existingPost -> {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setCategory(updatedPost.getCategory());
            existingPost.setLikes(updatedPost.getLikes());
            existingPost.setComments(updatedPost.getComments());
            return postsRepository.save(existingPost);
        });
    }

    public void deletePost(String idPosts) {
        postsRepository.deleteById(idPosts);
    }

    public Optional<Posts> addCommentToPost(String idPosts, Comments newComment) {
        return postsRepository.findById(idPosts).map(existingPost -> {
            existingPost.getComments().add(newComment);
            return postsRepository.save(existingPost);
        });
    }

    public boolean incrementLikes(String idPosts) {
        return postsRepository.findById(idPosts).map(post -> {
            post.setLikes(post.getLikes() + 1);
            postsRepository.save(post);
            return true;
        }).orElse(false);
    }

    public boolean decrementLikes(String idPosts) {
        return postsRepository.findById(idPosts).map(post -> {
            int currentLikes = post.getLikes();
            if (currentLikes > 0) {
                post.setLikes(currentLikes - 1);
                postsRepository.save(post);
            }
            return true;
        }).orElse(false);
    }

    public List<PostsDTO> searchCategory(String category) {
        return postsRepository.findByCategory(category)
                .stream()
                .map(PostsDTO::fromEntity)
                .toList();
    }

    public Map<String, Integer> countByCategory() {
        int beleza = Math.toIntExact(postsRepository.countByCategory("Beleza e cuidados"));
        int moda = Math.toIntExact(postsRepository.countByCategory("Moda e Estilo"));
        int gestacao = Math.toIntExact(postsRepository.countByCategory("Gestação"));
        int menstruacao = Math.toIntExact(postsRepository.countByCategory("Ciclo Menstrual"));
        int saude = Math.toIntExact(postsRepository.countByCategory("Saude e bem-estar"));

        Map<String, Integer> categorias = new LinkedHashMap<>(); // LinkedHashMap mantém a ordem de inserção
        categorias.put("Beleza e cuidados", beleza);
        categorias.put("Moda e Estilo", moda);
        categorias.put("Gestação", gestacao);
        categorias.put("Ciclo Menstrual", menstruacao);
        categorias.put("Saude e bem-estar", saude);

        return categorias;
    }
}