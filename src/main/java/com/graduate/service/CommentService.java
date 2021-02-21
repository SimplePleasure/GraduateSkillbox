package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.model.Post;
import com.graduate.model.PostComments;
import com.graduate.model.PostCommentsRepository;
import com.graduate.model.User;
import com.graduate.request.AddComment;
import com.graduate.response.ActionResultTemplateWithErrors;
import com.graduate.response.AddedComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final PostCommentsRepository commentsRepository;

    public CommentService(@Autowired PostCommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }


    public IResponse addComment(AddComment addComment, Post post, User user) throws RuntimeException {

        PostComments pc = new PostComments();
        if (addComment.getParentId() != 0) {
            if (post.getPostComments().stream().anyMatch(x -> x.getId() == addComment.getParentId())) {
                pc.setParentId(addComment.getParentId());
            } else {
                throw new RuntimeException();
            }
        }

        pc.setText(addComment.getText());
        pc.setTime(LocalDateTime.now());
        pc.setPost(post);
        pc.setUser(user);
        pc = commentsRepository.save(pc);
        return new AddedComment(pc.getId());
    }


}
