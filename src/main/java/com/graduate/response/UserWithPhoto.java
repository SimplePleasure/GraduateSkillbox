package com.graduate.response;

import com.graduate.base.UserAbs;

public class UserWithPhoto extends UserAbs {

    private String photo;

    public UserWithPhoto(int id, String name, String photo) {
        super(id, name);
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
