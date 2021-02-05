package com.graduate.response;

import com.graduate.base.IResponse;
import com.graduate.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostList implements IResponse {

    private int count;
    private List<PostForList> posts;

    public PostList(int count, List<Post> posts) {
        this.count = count;
        this.posts = posts
                .stream()
                .map(PostForList::new)
                .collect(Collectors.toList());
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public List<PostForList> getPosts() {
        return posts;
    }
    public void setPosts(List<PostForList> posts) {
        this.posts = posts;
    }
}
