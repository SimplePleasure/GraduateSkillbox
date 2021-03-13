package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.exceptionHandler.exceptions.ComponentNotFoundException;
import com.graduate.model.*;
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

    public IResponse addComment(AddComment addComment, Post post, User user) {
        if (addComment.getText().length() < 5) {
            ActionResultTemplateWithErrors response = new ActionResultTemplateWithErrors(false);
            response.addError("text", "comment text is too short");
            return response;
        }

        PostComments pc = new PostComments();
        if (addComment.getParentId() != 0) {
            if (post.getPostComments().stream().noneMatch(x -> x.getId() == addComment.getParentId())) {
                throw new ComponentNotFoundException("specified comment is missing from post");
            } else {
                pc.setParentId(addComment.getParentId());
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
