package com.graduate.base;

public abstract class UserAbs {
    // TODO: 29.01.2021 Is it necessary to extends ResponseAbs?

    private int id;
    private String name;

    public UserAbs(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
