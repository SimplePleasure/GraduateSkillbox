package com.graduate.response;

import com.graduate.base.IResponse;

import java.util.HashMap;
import java.util.Map;

public class ActionResultTemplateWithErrors implements IResponse {

    private boolean result;
    private Map<String, String> errors;

    public ActionResultTemplateWithErrors(boolean result, String key, String val) {
        this.result = result;
        this.errors = new HashMap<>();
        errors.put(key, val);
    }

    public void addError(String key, String val) {
        errors.put(key, val);
    }

    public boolean isResult() {
        return result;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}
