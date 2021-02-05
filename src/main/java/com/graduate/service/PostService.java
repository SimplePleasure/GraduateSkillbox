package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.model.Post;
import com.graduate.model.PostRepository;
import com.graduate.response.PostInstance;
import com.graduate.response.PostList;
import com.graduate.response.PostsCountForCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(@Autowired PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public IResponse getPostsByMode(int offset, int limit, String mode) {
        List<Post> posts;
        int postsCount = postRepository.getPostsCountByMode();
        switch (mode) {
            case "recent":
                posts = postRepository.findRecentPosts(offset, limit);
                break;
            case "early":
                posts = postRepository.findEarlyPosts(offset, limit);
                break;
            case "popular":
                posts = postRepository.findPopularPosts(offset, limit);
                break;
            case "best":
                posts = postRepository.findBestPosts(offset, limit);
                break;
            default:
                posts = Collections.emptyList();
        }

        return new PostList(postsCount, posts);
    }

    public IResponse getPostsBySearch(int offset, int limit, String searchQuery) {
        searchQuery = "%" + searchQuery + "%";
        int postsCount = postRepository.getPostsCountBySearchQuery(searchQuery);
        List<Post> posts = postRepository.findPostsBySearchQuery(offset, limit, searchQuery);
        return new PostList(postsCount, posts);
    }

    public IResponse getPostsByDate(int offset, int limit, String date) {
        date = date + "%";

        int postsCount = postRepository.getPostsCountByDate(date);
        List<Post> posts = postRepository.findPostsByDate(offset, limit, date);

        return new PostList(postsCount, posts);
    }

    public IResponse getPostsByTag(int offset, int limit, String tag) {
        Integer postCount = postRepository.getPostsCountByTag(tag);
        List<Post> posts = postRepository.findPostsByTag(offset, limit, tag);

        return new PostList(postCount, posts);
    }

    public IResponse getPostsByModerationStatus(int offset, int limit, String status) {
        // TODO: 17.01.2021 implement receiving moderator_id (add check is the current user a moderator?)
        int moderatorId = 1;

        int postsCount = postRepository.getPostsCountByModerationStatus(status, moderatorId);
        List<Post> posts = postRepository.findPostsByModerationStatus(offset, limit, status, moderatorId);

        return new PostList(postsCount, posts);
    }


    public IResponse getMyPosts(int offset, int limit, String status) {
        int userId = 1; // TODO: 17.01.2021 implement receiving user_id (is the user has auth)
        int isActive;
        String moderationStatus;

        switch(status) {
            case "inactive":
                isActive = 0;
                moderationStatus = "NEW";
                break;
            case "pending":
                isActive = 1;
                moderationStatus = "NEW";
                break;
            case "declined":
                isActive = 1;
                moderationStatus = "DECLINED";
                break;
            case "published":
                isActive = 1;
                moderationStatus = "ACCEPTED";
                break;
            default:
                // TODO: 19.01.2021 implement throw illegal state exception?
                isActive = 0;
                moderationStatus = null;
        }

        int postsCount = postRepository.getMyPostsCount(userId, isActive, moderationStatus);
        List<Post> posts = postRepository.findMyPosts(offset, limit, userId, isActive, moderationStatus);
        return new PostList(postsCount, posts);
    }

    public IResponse getPostById(int postId) {
        // TODO: 29.01.2021 optional.map() throwing nullpointer?
        Optional<Post> optional = postRepository.findById(postId);
        return optional.map(PostInstance::new).orElse(null);
    }

    public IResponse getPostsCountByDate(int year) { // TODO: 02.02.2021 implement object return
        List<Integer> yearsWithPosts = postRepository.getYearsWherePostExists();
        List<PostRepository.PostsCountByDate> postsCount = postRepository.getPostsCountGroupByDays(year);
        Map<LocalDate, Integer> postsCountByDate = new HashMap<>();

        postsCount.forEach(v -> postsCountByDate.put(v.getDate(), v.getCount()));
        return new PostsCountForCalendar(yearsWithPosts, postsCountByDate);
    }

}
