package com.graduate.core;

import com.graduate.base.IResponse;
import com.graduate.request.AddPost;
import com.graduate.response.ActionResultTemplateWithErrors;

public class ResponseFormer {

    public static IResponse getErrResponseFromPost(AddPost post) {
        ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
        if (post.isTextTooShort()) {
            result.addError("text", "text too short");
        }
        if (post.isTitleTooShort()) {
            result.addError("title", "title too short");
        }
        return result;
    }
}
