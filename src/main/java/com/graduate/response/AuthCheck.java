package com.graduate.response;

import com.graduate.base.IResponse;

public class AuthCheck implements IResponse {

    private boolean result;
    private UserInfo user;

    public AuthCheck(boolean result, UserInfo user) {
        this.result = result;
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public UserInfo getUser() {
        return user;
    }
    public void setUser(UserInfo user) {
        this.user = user;
    }
}
