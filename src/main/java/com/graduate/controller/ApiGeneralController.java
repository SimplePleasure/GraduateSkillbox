package com.graduate.controller;

import com.graduate.base.IResponse;
import com.graduate.response.BlogInformation;
import com.graduate.service.PostService;
import com.graduate.service.SettingsService;
import com.graduate.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RequestMapping(value = "api/")
@RestController
public class ApiGeneralController {
    private final BlogInformation blogInformation;
    private final SettingsService settings;
    private final TagService tagService;
    private final PostService postService;

    public ApiGeneralController(@Autowired BlogInformation blogInformation, @Autowired SettingsService settings,
                                @Autowired TagService tagService, @Autowired PostService postService) {
        this.blogInformation = blogInformation;
        this.settings = settings;
        this.tagService = tagService;
        this.postService = postService;
    }


    @GetMapping(value = "init", produces = "application/json")
    private ResponseEntity<IResponse> getMainInformation() {
        return ResponseEntity.status(HttpStatus.OK).body(blogInformation);
    }

    @GetMapping(value = "settings", produces = "application/json")
    private ResponseEntity<IResponse> getSettings() {
        return ResponseEntity.status(HttpStatus.OK).body(settings.getGlobalSettings());
    }


    // TODO: 02.02.2021 Проверить приходит ли null значения
    @GetMapping(value = "tag", produces = "application/json")
    private ResponseEntity<IResponse> getTags(@RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getTagWeight(query));
    }

    @GetMapping(value = "calendar", produces = "application/json")
    private ResponseEntity<IResponse> getPostsCountByDate(@RequestParam(required = false) Integer year) {
        year = year == null ? LocalDate.now().getYear() : year;
        IResponse result = postService.getPostsCountByDate(year);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
