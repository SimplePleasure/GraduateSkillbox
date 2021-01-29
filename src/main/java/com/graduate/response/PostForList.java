package com.graduate.response;

import com.graduate.model.Post;

public class PostForList extends PostAbs {

    private String announce;

    public PostForList(Post post) {
        super(post);
        announce = post.getText();
    }

    public String getAnnounce() {
        return announce;
    }
    public void setAnnounce(String announce) {
        this.announce = announce;
    }
}
