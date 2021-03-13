package com.graduate.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class Settings {

    @JsonProperty("MULTIUSER_MODE")
    boolean multiuser;
    @JsonProperty("POST_PREMODERATION")
    boolean premoderation;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    boolean statistic;

    public Settings(){
    }

    public boolean isMultiuser() {
        return multiuser;
    }
    public void setMultiuser(boolean multiuser) {
        this.multiuser = multiuser;
    }
    public boolean isStatistic() {
        return statistic;
    }
    public void setStatistic(boolean statistic) {
        this.statistic = statistic;
    }
    public boolean isPremoderation() {
        return premoderation;
    }
    public void setPremoderation(boolean premoderation) {
        this.premoderation = premoderation;
    }
}
