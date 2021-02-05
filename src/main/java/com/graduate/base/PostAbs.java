package com.graduate.base;

import com.graduate.model.Post;
import com.graduate.response.UserSimplify;

public abstract class PostAbs {


    private int id;
    private String timestamp;
    private UserAbs user;
    private String title;

    private long likeCount;
    private long dislikeCount;
    private long commentCount;
    private long viewCount;

    public PostAbs(Post post) {

        id = post.getId();
        timestamp = post.getTime().toString();
        user = new UserSimplify(post.getUser().getId(), post.getUser().getName());
        title = post.getTitle();
        viewCount = post.getViewCount();

        commentCount = post.getPostComments().size();
        likeCount = post.getPostVotes().stream().filter(x -> x.getValue() > 0).count();
        dislikeCount = post.getPostVotes().size() - likeCount;
    }


    public int getId() {
        return id;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public UserAbs getUser() {
        return user;
    }
    public String getTitle() {
        return title;
    }
    public long getLikeCount() {
        return likeCount;
    }
    public long getDislikeCount() {
        return dislikeCount;
    }
    public long getCommentCount() {
        return commentCount;
    }
    public long getViewCount() {
        return viewCount;
    }
}
