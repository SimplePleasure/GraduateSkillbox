package com.graduate.controller;

import com.graduate.base.IResponse;
import com.graduate.model.User;
import com.graduate.request.AddPost;
import com.graduate.request.Vote;
import com.graduate.service.AuthService;
import com.graduate.service.PostService;
import com.graduate.service.SettingsService;
import com.graduate.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {

    private final PostService postService;
    private final AuthService authService;
    private final VoteService voteService;
    private final SettingsService settingsService;

    public ApiPostController(@Autowired PostService postService, @Autowired AuthService authService,
                             @Autowired VoteService voteService, @Autowired SettingsService settingsService) {
        this.postService = postService;
        this.authService = authService;
        this.voteService = voteService;
        this.settingsService = settingsService;
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostsByMode(@RequestParam int offset,
                                                      @RequestParam int limit, @RequestParam String mode) {
        IResponse result = postService.getPostsByMode(offset, limit, mode);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostsBySearch(@RequestParam int offset,
                                                   @RequestParam int limit, @RequestParam String query) {
        IResponse result = postService.getPostsBySearch(offset, limit, query);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/byDate", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostsByDate(@RequestParam int offset,
                                                 @RequestParam int limit, @RequestParam String date) {
        IResponse result = postService.getPostsByDate(offset, limit, date);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/byTag", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostsByTag(@RequestParam int offset,
                                                @RequestParam int limit, @RequestParam String tag) {
        IResponse result = postService.getPostsByTag(offset, limit, tag);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostsByModerationStatus(@RequestParam int offset, @RequestParam int limit,
                                                                    @RequestParam String status, WebRequest request) {
        IResponse result = postService.getPostsByModerationStatus(offset, limit, status,
                                                    authService.getUserIdOrZero(), request);
        // TODO: 04.03.2021 after adding spring security access set getUserIdOrZero as getUserId
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getMyPosts(@RequestParam int offset,
                                             @RequestParam int limit, @RequestParam String status) {
        int userId = authService.getUserIdOrZero();
        IResponse result = postService.getMyPosts(offset, limit, status, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostById(@PathVariable("id") int postId, WebRequest request) {
        IResponse requestedPost = postService.getPostInstanceOrThrow(postId, authService.getUserIdOrZero(), request);
        return ResponseEntity.status(HttpStatus.OK).body(requestedPost);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IResponse> addPost(@RequestBody AddPost addPost) {
        IResponse result = postService.addPost(addPost, authService.getCurrentUserOrThrow(),
                settingsService.isPreModerationEnabled());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IResponse> editPost(@PathVariable int id, @RequestBody AddPost addPost, WebRequest request) {
        IResponse result = postService.editPost(addPost, id, request, settingsService.isPreModerationEnabled());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/like" , method = RequestMethod.POST)
    public ResponseEntity<IResponse> like(@RequestBody Vote vote) {

        User user = authService.getCurrentUserOrThrow();
        IResponse result = voteService.vote(vote.getPostId(), user.getId(), Byte.parseByte("1"));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/dislike" , method = RequestMethod.POST)
    public ResponseEntity<IResponse> dislike(@RequestBody Vote vote) {

        User user = authService.getCurrentUserOrThrow();
        IResponse result = voteService.vote(vote.getPostId(), user.getId(), Byte.parseByte("-1"));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
