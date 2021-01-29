package com.graduate.response;

public class UserWithPhoto extends UserAbs{

    private String photo;

    public UserWithPhoto(int id, String name, String photo) {
        super(id, name);
        this.photo = photo;
    }
}
