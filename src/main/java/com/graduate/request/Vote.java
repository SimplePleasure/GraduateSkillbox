package com.graduate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vote {
    @JsonProperty("post_id")
    private int postId;

    public Vote() {}

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
}
