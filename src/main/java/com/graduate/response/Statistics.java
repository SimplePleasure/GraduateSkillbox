package com.graduate.response;

import com.graduate.base.IResponse;

import java.time.LocalDateTime;

public class Statistics implements IResponse {

    private int postsCount;
    private int likesCount;
    private int dislikesCount;
    private int viewsCount;
    private LocalDateTime firstPublication;

    public Statistics(int postsCount, int likesCount, int dislikesCount, int viewsCount, LocalDateTime firstPublication) {
        this.postsCount = postsCount;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.viewsCount = viewsCount;
        this.firstPublication = firstPublication;
    }
    public Statistics(){}

    public int getPostsCount() {
        return postsCount;
    }
    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }
    public int getLikesCount() {
        return likesCount;
    }
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
    public int getDislikesCount() {
        return dislikesCount;
    }
    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }
    public int getViewsCount() {
        return viewsCount;
    }
    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }
    public LocalDateTime getFirstPublication() {
        return firstPublication;
    }
    public void setFirstPublication(LocalDateTime firstPublication) {
        this.firstPublication = firstPublication;
    }
}
