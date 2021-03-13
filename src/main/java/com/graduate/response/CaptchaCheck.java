package com.graduate.response;

import com.graduate.base.IResponse;

public class CaptchaCheck implements IResponse {

    private String secret;
    private String image;

    public CaptchaCheck(String secret, String image) {
        this.secret = secret;
        this.image = image;
    }

    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}