package com.graduate.response;

import com.graduate.base.IResponse;

public class Denied implements IResponse {
    private boolean result = false;
    public boolean isResult() {
        return result;
    }
}
