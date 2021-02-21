package com.graduate.response;

import com.graduate.base.IResponse;

public class ActionResultTemplate implements IResponse {
    private boolean result;

    public ActionResultTemplate(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
