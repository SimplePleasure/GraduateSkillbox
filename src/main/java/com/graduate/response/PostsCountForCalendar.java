package com.graduate.response;

import com.graduate.base.IResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PostsCountForCalendar implements IResponse {

    private List<Integer> years;
    private Map<LocalDate, Integer> posts;

    public PostsCountForCalendar(List<Integer> years, Map<LocalDate, Integer> posts) {
        this.years = years;
        this.posts = posts;
    }

    public List<Integer> getYears() {
        return years;
    }
    public void setYears(List<Integer> years) {
        this.years = years;
    }
    public Map<LocalDate, Integer> getPosts() {
        return posts;
    }
    public void setPosts(Map<LocalDate, Integer> posts) {
        this.posts = posts;
    }
}
