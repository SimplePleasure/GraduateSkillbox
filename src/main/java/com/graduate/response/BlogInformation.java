package com.graduate.response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class BlogInformation {
    @Value("${blog.information.title}")
    String title;
    @Value("${blog.information.subtitle}")
    String subtitle;
    @Value("${blog.information.phone}")
    String phone;
    @Value("${blog.information.email}")
    String email;
    @Value("${blog.information.copyright}")
    String copyright;
    @Value("${blog.information.copyrightFrom}")
    String copyrightFrom;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCopyright() {
        return copyright;
    }
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    public String getCopyrightFrom() {
        return copyrightFrom;
    }
    public void setCopyrightFrom(String copyrightFrom) {
        this.copyrightFrom = copyrightFrom;
    }
}
