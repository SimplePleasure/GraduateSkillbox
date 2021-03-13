package com.graduate.request;

import org.springframework.stereotype.Component;

@Component
public class Email {
    private String email;

    public Email() {}

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
