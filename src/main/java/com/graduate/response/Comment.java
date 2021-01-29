package com.graduate.response;

import com.graduate.model.PostComments;

public class Comment {

    private int id;
    private String timestamp;
    private String text;
    private UserWithPhoto user;

//    public Comments(int id, String timestamp, String text, UserWithPhoto user) {
//        this.id = id;
//        this.timestamp = timestamp;
//        this.text = text;
//        this.user = user;
//    }

    public Comment(PostComments postComments) {
        id = postComments.getId();
        timestamp = postComments.getTime().toString();
        text = postComments.getText();
        user = new UserWithPhoto(postComments.getUser().getId(),
                postComments.getUser().getName(), postComments.getUser().getPhoto());
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public UserWithPhoto getUser() {
        return user;
    }
    public void setUser(UserWithPhoto user) {
        this.user = user;
    }
}
