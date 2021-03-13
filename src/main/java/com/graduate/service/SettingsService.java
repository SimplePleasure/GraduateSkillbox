package com.graduate.service;

import com.graduate.model.GlobalSettingsRepository;
import com.graduate.request.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SettingsService {
    private final GlobalSettingsRepository settingsRepository;

    public SettingsService(@Autowired GlobalSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Map<String, Boolean> getGlobalSettings() {
        return settingsRepository.getSettings().stream().collect(Collectors.toMap(
                GlobalSettingsRepository.Settings::getCode, GlobalSettingsRepository.Settings::getValue));
    }

    public void setSettings(Settings settings, WebRequest request) {
        if (request.isUserInRole("1")) {
            settingsRepository.setSettings(settings.isMultiuser(), settings.isPremoderation(), settings.isStatistic());
        }
    }

    public boolean isGlobalStatisticPublic() {
        return settingsRepository.isParameterEnabled("STATISTICS_IS_PUBLIC").getValue();
    }

    public boolean isPreModerationEnabled() {
        return settingsRepository.isParameterEnabled("POST_PREMODERATION").getValue();
    }
}
