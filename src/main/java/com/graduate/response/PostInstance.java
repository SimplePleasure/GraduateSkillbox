package com.graduate.response;

import com.graduate.base.PostAbs;
import com.graduate.model.ModerationStatus;
import com.graduate.model.Tags;
import com.graduate.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostInstance extends PostAbs {

    private boolean active;
    private String text;
    private List<Comment> comments;
    private List<String> tags;

    public PostInstance(Post post) {
        super(post);
        text = post.getText();
        active = post.getIsActive() > 0 && post.getModerationStatus() == ModerationStatus.ACCEPTED;
        comments = post.getPostComments().stream().map(Comment::new).collect(Collectors.toList());
        tags = post.getTags().stream().map(Tags::getName).collect(Collectors.toList());
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
