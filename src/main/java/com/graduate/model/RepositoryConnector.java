package com.graduate.model;

import com.graduate.Mapper;
import com.graduate.response.PostInstance;
import com.graduate.response.TransformToResponsePostList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryConnector {

    private static PostRepository postRepository;
    private static UserRepository userRepository;

    public RepositoryConnector(@Autowired PostRepository postRepository, @Autowired UserRepository userRepository) {
        RepositoryConnector.userRepository = userRepository;
        RepositoryConnector.postRepository = postRepository;
    }

    public static PostRepository getPostRepository() {
        return postRepository;
    }
    public static UserRepository getUserRepository() {
        return userRepository;
    }


    public static String getPostsByMode(int offset, int limit, String mode) {
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

        TransformToResponsePostList response = new TransformToResponsePostList(postsCount, posts);
        return Mapper.getJSON(response);
    }

    public static String getPostsBySearch(int offset, int limit, String searchQuery) {
        searchQuery = "%" + searchQuery + "%";
        int postsCount = postRepository.getPostsCountBySearchQuery(searchQuery);
        List<Post> posts = postRepository.findPostsBySearchQuery(offset, limit, searchQuery);

        TransformToResponsePostList response = new TransformToResponsePostList(postsCount, posts);
        return Mapper.getJSON(response);
    }

    public static String getPostsByDate(int offset, int limit, String date) {
        date = date + "%";

        int postsCount = postRepository.getPostsCountByDate(date);
        List<Post> posts = postRepository.findPostsByDate(offset, limit, date);

        TransformToResponsePostList response = new TransformToResponsePostList(postsCount, posts);
        return Mapper.getJSON(response);
    }

    public static String getPostsByTag(int offset, int limit, String tag) {
        Integer postCount = postRepository.getPostsCountByTag(tag);
        List<Post> posts = postRepository.findPostsByTag(offset, limit, tag);

        TransformToResponsePostList response = new TransformToResponsePostList(postCount, posts);
        return Mapper.getJSON(response);
    }

    public static String getPostsByModerationStatus(int offset, int limit, String status) {
        // TODO: 17.01.2021 implement receiving moderator_id (add check is the current user a moderator?)
        int moderatorId = 1;

        int postsCount = postRepository.getPostsCountByModerationStatus(status, moderatorId);
        List<Post> posts = postRepository.findPostsByModerationStatus(offset, limit, status, moderatorId);

        TransformToResponsePostList response = new TransformToResponsePostList(postsCount, posts);
        return Mapper.getJSON(response);
    }


    public static String getMyPosts(int offset, int limit, String status) {
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

        TransformToResponsePostList response = new TransformToResponsePostList(postsCount, posts);
        return Mapper.getJSON(response);
    }

    public static String getPostById(int postId) {
        Optional<Post> optional = postRepository.findById(postId);
        return optional.isPresent() ? Mapper.getJSON(new PostInstance(optional.get())) : "";
    }

}
