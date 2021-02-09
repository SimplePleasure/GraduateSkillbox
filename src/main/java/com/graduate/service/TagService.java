package com.graduate.service;

import com.graduate.model.Tag2PostRepository;
import com.graduate.response.TagsWeight;
import org.springframework.stereotype.Service;


@Service
public class TagService {
    Tag2PostRepository tag2PostRepository;

    public TagService(Tag2PostRepository tag2PostRepository) {
        this.tag2PostRepository = tag2PostRepository;
    }

    public TagsWeight getTagWeight(String query) {
        return new TagsWeight(tag2PostRepository.getTags("%" + query + "%"));
    }
}
