package com.graduate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalSettings {

    @JsonProperty(value = "MULTIUSER_MODE")
    boolean multiUserMode;
    @JsonProperty(value = "POST_PREMODERATION")
    boolean postPremoderation;
    @JsonProperty(value = "STATISTICS_IS_PUBLIC")
    boolean statisticsIsPublic;

    public boolean isMultiUserMode() {
        return multiUserMode;
    }
    public void setMultiUserMode(boolean multiUserMode) {
        this.multiUserMode = multiUserMode;
    }
    public boolean isPostPremoderation() {
        return postPremoderation;
    }
    public void setPostPremoderation(boolean postPremoderation) {
        this.postPremoderation = postPremoderation;
    }
    public boolean isStatisticsIsPublic() {
        return statisticsIsPublic;
    }
    public void setStatisticsIsPublic(boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }
}