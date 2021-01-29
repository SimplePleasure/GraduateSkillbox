package com.graduate.service;

import com.graduate.model.GlobalSettingsRepository;
import com.graduate.response.GlobalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private final GlobalSettingsRepository settingsRepository;

    public SettingsService(@Autowired GlobalSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public GlobalSettings getGlobalSettings() {
        return new GlobalSettings();
    }
}
