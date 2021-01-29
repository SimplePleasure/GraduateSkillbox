package com.graduate.controller;

import com.graduate.response.BlogInformation;
import com.graduate.response.GlobalSettings;
import com.graduate.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {
    private final BlogInformation blogInformation;
    private SettingsService settings;

    public ApiGeneralController(@Autowired BlogInformation blogInformation, @Autowired SettingsService settingsService) {
        this.blogInformation = blogInformation;
        settings = settingsService;
    }


    @GetMapping(value = "api/init", produces = "application/json")
    public BlogInformation getMainInformation() {
        return blogInformation;
    }

    @GetMapping(value = "api/settings", produces = "application/json")
    private GlobalSettings getSettings() {
        return settings.getGlobalSettings();
    }
}
