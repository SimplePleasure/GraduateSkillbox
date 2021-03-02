package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.core.ComputePages;
import com.graduate.exceptionHandler.exceptions.PostNotFoundException;
import com.graduate.exceptionHandler.exceptions.UnknownInputStatusException;
import com.graduate.exceptionHandler.exceptions.UserNotAuthException;
import com.graduate.model.*;
import com.graduate.request.AddPost;
import com.graduate.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(@Autowired PostRepository postRepository) {
        this.postRepository = postRepository;
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
        Integer postCount = postRepository.getPostsCountByTag(tag);
        List<Post> posts = postRepository.findPostsByTag(
                PageRequest.of(ComputePages.computePage(offset, limit), limit), tag);

        return new PostList(postCount, posts);
    }

    public IResponse getPostsByModerationStatus(int offset, int limit, String status, int moderatorId, WebRequest request) {
        if (request.isUserInRole("1")) {
            int postsCount = postRepository.getPostsCountByModerationStatus(status, moderatorId);
            List<Post> posts = postRepository.findPostsByModerationStatus(
                    PageRequest.of(ComputePages.computePage(offset, limit), limit), status, moderatorId);
            System.err.println(postsCount);
            System.err.println(posts.size());
            return new PostList(postsCount, posts);
        }
        return new PostList(0, Collections.emptyList());
    }


    public IResponse getMyPosts(int offset, int limit, String status, int userId) {
//        if (userId == 0) { // TODO: 02.03.2021 implement this check with spring access
//            throw new UserNotAuthException("executing action required auth");  // TODO: 02.03.2021 implement receiving msg from separate file
//        }
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
                throw new UnknownInputStatusException("unknown incoming status ");
        }

        int postsCount = postRepository.getMyPostsCount(userId, isActive, moderationStatus);
        List<Post> posts = postRepository.findMyPosts(PageRequest.of(ComputePages.computePage(offset, limit), limit),
                userId, isActive, moderationStatus);
        return new PostList(postsCount, posts);
    }

    public IResponse getPostInstanceOrThrow(int postId, int viewerId, WebRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (!request.isUserInRole("1") || post.getUser().getId() != viewerId) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }
        return new PostInstance(post);
    }

    public Post getPostByIdOrThrow(int postId) {
        Optional<Post> optional = postRepository.findById(postId);
        return optional.orElseThrow(PostNotFoundException::new);
    }

    public IResponse getPostsCountByDate(int year) {
        List<Integer> yearsWithPosts = postRepository.getYearsWherePostExists();
        List<PostRepository.PostsCountByDate> postsCount = postRepository.getPostsCountGroupByDays(year);
        Map<LocalDate, Integer> postsCountByDate = new HashMap<>();

        postsCount.forEach(v -> postsCountByDate.put(v.getDate(), v.getCount()));
        return new PostsCountForCalendar(yearsWithPosts, postsCountByDate);
    }

    public IResponse addPost(AddPost addPost, User user) {
        if (addPost.getTitle().length() < 3 || addPost.getText().length() < 50) {
            ActionResultTemplateWithErrors result = new ActionResultTemplateWithErrors(false);
            if (addPost.getTitle().length() < 3) {
                result.addError("title", "title is too short");
            }
            if (addPost.getText().length() < 50) {
                result.addError("text", "text is too short");
            }
            return result;
        }

        Post post = new Post(addPost.getActive(), ModerationStatus.NEW, addPost.getTimestamp(), addPost.getText(), addPost.getTitle(), user);
        List<Tags> tags = addPost.getTags().stream().map(Tags::new).collect(Collectors.toList());
        post.setTags(tags);
        postRepository.save(post);

        return new ActionResultTemplate(true);
    }

    public int getPostsCountWaitingModeration() {
        return postRepository.getPostsCountWaitingModeration();
    }



}
