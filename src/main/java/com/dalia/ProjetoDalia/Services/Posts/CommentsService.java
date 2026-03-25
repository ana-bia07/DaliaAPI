package com.dalia.ProjetoDalia.Services.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.CommentsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Model.Repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final PostsRepository postsRepository;

    public Optional<Comments> addComment(String postId, CommentsDTO commentdto) {
        Optional<Posts> postOpt = postsRepository.findById(postId);
        if (postOpt.isPresent()) {
            Posts post = postOpt.get();
            post.getComments().add(commentdto.toEntity());
            postsRepository.save(post);
            return Optional.of(commentdto.toEntity());
        }
        return Optional.empty();
    }

    public List<Comments> getCommentsByPostId(String postId) {
        return postsRepository.findById(postId)
                .map(Posts::getComments)
                .orElse(Collections.emptyList());
    }
}
