package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.core.ComputePages;
import com.graduate.model.*;
import com.graduate.request.AddComment;
import com.graduate.response.PostInstance;
import com.graduate.response.PostList;
import com.graduate.response.PostsCountForCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(@Autowired PostRepository postRepository, @Autowired UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public IResponse getPostsByMode(int offset, int limit, String mode) {
        List<Post> posts;
        int postsCount = postRepository.getPostsCountByMode();
        PageRequest pr = PageRequest.of(ComputePages.computePage(offset, limit), limit);
        switch (mode) {
            case "recent":
                posts = postRepository.findRecentPosts(pr);
                break;
            case "early":
                posts = postRepository.findEarlyPosts(pr);
                break;
            case "popular":
                posts = postRepository.findPopularPosts(pr);
                break;
            case "best":
                posts = postRepository.findBestPosts(pr);
                break;
            default:
                posts = Collections.emptyList();
        }

        return new PostList(postsCount, posts);
    }

    public IResponse getPostsBySearch(int offset, int limit, String searchQuery) {
        searchQuery = "%" + searchQuery + "%";
        int postsCount = postRepository.getPostsCountBySearchQuery(searchQuery);
        List<Post> posts = postRepository.findPostsBySearchQuery(
                PageRequest.of(ComputePages.computePage(offset, limit), limit), searchQuery);
        return new PostList(postsCount, posts);
    }

    public IResponse getPostsByDate(int offset, int limit, String date) {
        date = date + "%";
        int postsCount = postRepository.getPostsCountByDate(date);
        List<Post> posts = postRepository.findPostsByDate(
                PageRequest.of(ComputePages.computePage(offset, limit), limit), date);
        return new PostList(postsCount, posts);
    }

    public IResponse getPostsByTag(int offset, int limit, String tag) {
        // TODO: 08.02.2021 can use primitive type here?
        Integer postCount = postRepository.getPostsCountByTag(tag);
        List<Post> posts = postRepository.findPostsByTag(
                PageRequest.of(ComputePages.computePage(offset, limit), limit), tag);

        return new PostList(postCount, posts);
    }

    public IResponse getPostsByModerationStatus(int offset, int limit, String status) {
        // TODO: 17.01.2021 implement receiving moderator_id (add check is the current user a moderator?)

        int moderatorId = 1;

        int postsCount = postRepository.getPostsCountByModerationStatus(status, moderatorId);
        List<Post> posts = postRepository.findPostsByModerationStatus(
                PageRequest.of(ComputePages.computePage(offset, limit), limit), status, moderatorId);
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
        List<Post> posts = postRepository.findMyPosts(PageRequest.of(ComputePages.computePage(offset, limit), limit),
                userId, isActive, moderationStatus);
        return new PostList(postsCount, posts);
    }

    public IResponse getPostById(int postId) {
        // TODO: 20.02.2021 increment post counter while viewer is not author or moderator
        // TODO: 29.01.2021 optional.map() throwing nullpointer?
        Optional<Post> optional = postRepository.findById(postId);
        return optional.map(PostInstance::new).orElse(null);
    }

    public Post getPostByIdOrThrow(int postId) {
        Optional<Post> optional = postRepository.findById(postId);
        return optional.orElseThrow(RuntimeException::new);
    }

    public IResponse getPostsCountByDate(int year) { // TODO: 02.02.2021 implement object return
        List<Integer> yearsWithPosts = postRepository.getYearsWherePostExists();
        List<PostRepository.PostsCountByDate> postsCount = postRepository.getPostsCountGroupByDays(year);
        Map<LocalDate, Integer> postsCountByDate = new HashMap<>();

        postsCount.forEach(v -> postsCountByDate.put(v.getDate(), v.getCount()));
        return new PostsCountForCalendar(yearsWithPosts, postsCountByDate);
    }

}
