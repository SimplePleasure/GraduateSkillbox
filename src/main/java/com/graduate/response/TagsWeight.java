package com.graduate.response;

import com.graduate.base.IResponse;
import com.graduate.model.Tag2PostRepository;

import java.util.List;

public class TagsWeight implements IResponse {
    private List<Tag2PostRepository.TagWeight> tags;

    public TagsWeight(List<Tag2PostRepository.TagWeight> tags) {
        this.tags = tags;
    }

    public void setTags(List<Tag2PostRepository.TagWeight> tags) {
        this.tags = tags;
    }
    public List<Tag2PostRepository.TagWeight> getTags() {
        return tags;
    }

}
