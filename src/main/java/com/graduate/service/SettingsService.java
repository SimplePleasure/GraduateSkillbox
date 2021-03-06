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

    // TODO: 31.01.2021 implement receive settings from repository
    public GlobalSettings getGlobalSettings() {
        GlobalSettings gs = new GlobalSettings();
        gs.setMultiUserMode(true);
        gs.setPostPremoderation(true);
        gs.setStatisticsIsPublic(true);
        return gs;
    }
}
