package com.graduate.request;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

// TODO: 22.02.2021 Нужно ли аннотация component?
@Component
public class AddPost {

    private LocalDateTime timestamp;
    private byte active;
    private String title;
    private List<String> tags;
    private String text;

    public AddPost() {}

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        LocalDateTime publishedTime = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(publishedTime)) {
            this.timestamp = currentTime;
        } else {
            this.timestamp = publishedTime;
        }
    }

    public byte getActive() {
        return active;
    }
    public void setActive(byte active) {
        this.active = active;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}