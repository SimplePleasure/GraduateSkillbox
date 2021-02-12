package com.graduate.response;

import com.graduate.base.UserAbs;

public class UserInfo extends UserAbs {

    private String photo;
    private String email;
    private boolean moderation;
    private int moderationCount;
        // TODO: 11.02.2021 set settings /api/auth/login   /api/auth/check
    private boolean settings = false;

    public UserInfo(int id, String name, String photo, String email, boolean moderation, int moderationCount, boolean settings) {
        super(id, name);
        this.photo = photo;
        this.email = email;
        this.moderation = moderation;
        this.moderationCount = moderationCount;
        this.settings = settings;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isModeration() {
        return moderation;
    }
    public void setModeration(boolean moderation) {
        this.moderation = moderation;
    }
    public int getModerationCount() {
        return moderationCount;
    }
    public void setModerationCount(int moderationCount) {
        this.moderationCount = moderationCount;
    }
    public boolean isSettings() {
        return settings;
    }
    public void setSettings(boolean settings) {
        this.settings = settings;
    }
}
