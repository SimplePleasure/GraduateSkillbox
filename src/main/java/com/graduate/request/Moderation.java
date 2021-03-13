package com.graduate.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class Moderation {
    @JsonProperty("post_id")
    private int postId;
    private String decision;

    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public String getDecision() {
        return decision;
    }
    public void setDecision(String decision) {
        this.decision = decision;
    }
}
