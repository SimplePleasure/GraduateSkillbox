package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.core.ComputePages;
import com.graduate.core.FormAnResponse;
import com.graduate.exceptionHandler.exceptions.PostNotFoundException;
import com.graduate.exceptionHandler.exceptions.UnknownInputStatusException;
import com.graduate.exceptionHandler.exceptions.UserNotAuthException;
import com.graduate.model.*;
import com.graduate.request.AddPost;
import com.graduate.request.Moderation;
import com.graduate.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            return new PostList(postsCount, posts);
        }
        return new PostList(0, Collections.emptyList());
    }


    public IResponse getMyPosts(int offset, int limit, String status, int userId) {
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
        if (!request.isUserInRole("1") && !(post.getUser().getId() == viewerId)) {
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

    public IResponse addPost(AddPost addPost, User user, boolean premoderation) {
        if (addPost.isTextTooShort() || addPost.isTitleTooShort()) {
            return FormAnResponse.getErrResponseFromPost(addPost.isTextTooShort(), addPost.isTitleTooShort());
        }
        ModerationStatus status = premoderation ? ModerationStatus.NEW : ModerationStatus.ACCEPTED;
        Post post = new Post(addPost.getActive(), status, addPost.getTimestamp(), addPost.getText(), addPost.getTitle(), user);
        List<Tags> tags = addPost.getTags().stream().map(Tags::new).collect(Collectors.toList());
        post.setTags(tags);
        postRepository.save(post);
        return new ActionResultTemplate(true);
    }

    public int getPostsCountWaitingModeration() {
        return postRepository.getPostsCountWaitingModeration();
    }


    // TODO: 05.03.2021 implement sync methods for tags & remove orphan tag
    public IResponse editPost(AddPost addPost, int postId, WebRequest request, boolean premoderation) {
        if (addPost.isTitleTooShort() || addPost.isTextTooShort()) {
            return FormAnResponse.getErrResponseFromPost(addPost.isTextTooShort(), addPost.isTitleTooShort());
        }
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (post.getUser().getEmail().equals(currentEmail) || request.isUserInRole("1")) {
            post.setTags(addPost.getTags().stream().map(Tags::new).collect(Collectors.toList()));
            post.setTitle(addPost.getTitle());
            post.setText(addPost.getText());
            post.setIsActive(addPost.getActive());
            post.setTime(addPost.getTimestamp());
            if (!request.isUserInRole("1")) {
                ModerationStatus status = premoderation ? ModerationStatus.NEW : ModerationStatus.ACCEPTED;
                post.setModerationStatus(status);
                post.setModeratorId(null);
            }
            postRepository.save(post);
            return new ActionResultTemplate(true);
        }
        throw new UserNotAuthException("this user can't edit this post");
    }

    public IResponse getMyStatistics(int userId) {
        // TODO: 10.03.2021 Стоит ли переприсать подсчёт статистики на sql запросы?
        List<Post> myPosts = postRepository.getAvailablePostsByUserId(userId);
        if (myPosts.size() > 0) {
            List<PostVotes> postVotes = myPosts.stream().flatMap(x -> x.getPostVotes().stream()).collect(Collectors.toList());
            LocalDateTime earlyPost = myPosts.stream().min(Comparator.comparing(Post::getTime)).map(Post::getTime).get();

            int postsCount = myPosts.size();
            int viewCount = myPosts.stream().map(Post::getViewCount).reduce(0, Integer::sum);
            int likes = (int)postVotes.stream().filter(x -> x.getValue() == 1).count();
            int dislikes = (int)postVotes.stream().filter(x -> x.getValue() == -1).count();
            return new Statistics(postsCount, likes, dislikes, viewCount, earlyPost);
        } else {
            return new Statistics();
        }
    }

    public IResponse getAllStatistics() {
        int postCount = postRepository.getPostsCount();
        if (postCount > 0) {
            int viewsCount = postRepository.getViewsCount();
            LocalDateTime time = postRepository.getTime();
            PostRepository.Votes votes = postRepository.getVotesCount();
            return new Statistics(postCount, votes.getLikes(), votes.getDislikes(), viewsCount, time);
        }
        return new Statistics();
    }

    public IResponse moderation(Moderation moderation, int moderatorId, WebRequest request) {
        if (request.isUserInRole("1")) {
            Post post = postRepository.findById(moderation.getPostId()).orElseThrow(PostNotFoundException::new);
            switch (moderation.getDecision()) {
                case "accept":
                    post.setModerationStatus(ModerationStatus.ACCEPTED);
                    break;
                case "decline":
                    post.setModerationStatus(ModerationStatus.DECLINED);
                    break;
            }
            post.setModeratorId(moderatorId);
            postRepository.save(post);
            return new ActionResultTemplate(true);
        }
        throw new UserNotAuthException("Action requires moderation access");
    }

}
