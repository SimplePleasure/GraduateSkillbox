package com.graduate.response;

import com.graduate.base.IResponse;

public class AddedComment implements IResponse {

    private int id;

    public AddedComment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
