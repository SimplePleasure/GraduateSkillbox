package com.graduate.response;

import com.graduate.base.IResponse;

public class ActionResult implements IResponse {
    private boolean result = false;

    public ActionResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}
