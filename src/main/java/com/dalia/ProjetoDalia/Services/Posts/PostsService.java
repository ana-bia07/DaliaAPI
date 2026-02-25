package com.dalia.ProjetoDalia.Services.Posts;

import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Model.Repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Posts> getAllPosts() {
        return postsRepository.findAll();
    }

    public Optional<Posts> updatePost(String idPosts, Posts updatedPost) {
        return postsRepository.findById(idPosts).map(existingPost -> {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
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
}