package com.graduate.core;

import com.graduate.base.IResponse;
import com.graduate.response.ActionResultTemplateWithErrors;

public class ResponseFormer {

    public static IResponse getErrResponseFromPost(boolean isTextTooShort, boolean isTitleTooShort) {
        ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
        if (isTextTooShort) {
            result.addError("text", "text too short");
        }
        if (isTitleTooShort) {
            result.addError("title", "title too short");
        }
        return result;
    }

    public static IResponse getErrFromRegister(boolean passwordLengthCorrect, boolean emailIsFree,
                                               boolean captchaCodeCorrect, boolean captchaTimeCorrect) {
        ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
        if (!passwordLengthCorrect) {
            result.addError("password", "password length less then 6 chars");
        }
        if (!emailIsFree) {
            result.addError("email", "this email address already exists");
        }
        if (!captchaCodeCorrect) {
            result.addError("captcha", "incorrect captcha code");
        }
        if (!captchaTimeCorrect) {
            result.addError("captcha", "captcha code is deprecated");
        }
        return result;
    }
}
