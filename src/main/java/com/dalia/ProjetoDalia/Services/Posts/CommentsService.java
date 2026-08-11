package com.dalia.ProjetoDalia.Services.Posts;

import com.dalia.ProjetoDalia.Model.DTOS.Posts.CommentsDTO;
import com.dalia.ProjetoDalia.Model.Entity.Comments;
import com.dalia.ProjetoDalia.Model.Entity.Posts;
import com.dalia.ProjetoDalia.Model.Repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final PostsRepository postsRepository;

    public Optional<Comments> addComment(String postId, Comments novoComentario) {
        return postsRepository.findById(postId).map(post -> {
            // Se a lista de comentários estiver nula, inicializamos ela
            if (post.getComments() == null) {
                post.setComments(new ArrayList<>());
            }

            // Adicionamos a Entity de comentário na lista do Post
            post.getComments().add(novoComentario);

            // Salvamos o Post (que agora contém o novo comentário)
            postsRepository.save(post);

            return novoComentario;
        });
    }

    public List<Comments> getCommentsByPostId(String postId) {
        return postsRepository.findById(postId)
                .map(Posts::getComments)
                .orElse(Collections.emptyList());
    }

    public void deleteComments(String idPost, int indexComment) {
        System.out.println(idPost + " " + indexComment);
        Posts post = postsRepository.findById(idPost)
                .orElseThrow(() -> new RuntimeException("publicação não existe"));
        List<Comments> comments = post.getComments();
        System.out.println("post: " + post);
        System.out.println("cometarios: " + comments);

        if(comments == null || indexComment < 0 || indexComment >= comments.size()) {
            throw  new RuntimeException("Comentario não existe");
        }

        comments.remove(indexComment);
        postsRepository.save(post);
    }

}
