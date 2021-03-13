package com.graduate.controller;

import com.graduate.base.IResponse;
import com.graduate.exceptionHandler.exceptions.UserNotAuthException;
import com.graduate.model.Post;
import com.graduate.model.User;
import com.graduate.request.AddComment;
import com.graduate.request.Moderation;
import com.graduate.request.Profile;
import com.graduate.request.Settings;
import com.graduate.response.BlogInformation;
import com.graduate.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;


@RequestMapping(value = "/api")
@RestController
public class ApiGeneralController {
    private final BlogInformation blogInformation;

    private final SettingsService settingsService;
    private final TagService tagService;
    private final PostService postService;
    private final AuthService authService;
    private final CommentService commentService;

    public ApiGeneralController(@Autowired BlogInformation blogInformation, @Autowired SettingsService settingsService,
                                @Autowired TagService tagService, @Autowired PostService postService,
                                @Autowired AuthService authService, @Autowired CommentService commentService) {
        this.blogInformation = blogInformation;
        this.settingsService = settingsService;
        this.tagService = tagService;
        this.postService = postService;
        this.authService = authService;
        this.commentService = commentService;
    }


    @GetMapping(value = "/init", produces = "application/json")
    private ResponseEntity<IResponse> getMainInformation() {
        return ResponseEntity.status(HttpStatus.OK).body(blogInformation);
    }

    @GetMapping(value = "/settings", produces = "application/json")
    private ResponseEntity<Map<String, Boolean>> getSettingsService() {
        return ResponseEntity.status(HttpStatus.OK).body(settingsService.getGlobalSettings());
    }

    @PutMapping(value = "/settings", consumes = "application/json")
    private void saveSettings(@RequestBody Settings settings, WebRequest request) {
        settingsService.setSettings(settings, request);
    }

    @GetMapping(value = "/tag", produces = "application/json")
    private ResponseEntity<IResponse> getTags(@RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getTagWeight(query));
    }

    @GetMapping(value = "/calendar", produces = "application/json")
    private ResponseEntity<IResponse> getPostsCountByDate(@RequestParam(required = false) Integer year) {
        year = year == null ? LocalDate.now().getYear() : year;
        IResponse result = postService.getPostsCountByDate(year);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @PostMapping(value = "/comment", consumes = "application/json")
    private ResponseEntity<IResponse> comment(@RequestBody AddComment addComment) {
        Post post = postService.getPostByIdOrThrow(addComment.getPostId());
        User user = authService.getCurrentUserOrThrow();
        IResponse response = commentService.addComment(addComment, post, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    private String uploadImage(@RequestParam MultipartFile image) {
        return authService.uploadImage(image);
    }

    @GetMapping(value = "/statistics/my", produces = "application/json")
    private ResponseEntity<IResponse> getMyStatistics() {
        int userId = authService.getUserIdOrZero();
        IResponse result = postService.getMyStatistics(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(value = "/statistics/all", produces = "application/json")
    private ResponseEntity<IResponse> getStatistics(WebRequest request) {
        if (!settingsService.isGlobalStatisticPublic() && !request.isUserInRole("1")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        IResponse result = postService.getAllStatistics();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(value = "/moderation")
    private ResponseEntity<IResponse> moderation(@RequestBody Moderation moderation, WebRequest request) {
        int moderatorId = authService.getUserIdOrZero();
        IResponse result = postService.moderation(moderation, moderatorId, request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(value = "/profile/my", consumes = "application/json")
    private ResponseEntity<IResponse> editProfile(@RequestBody Profile profile) {
        System.err.println(profile.getRemovePhoto());
        IResponse result = authService.setProfile(null, profile.getName(), profile.getEmail(),
                profile.getPassword(), profile.getRemovePhoto());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(value = "/profile/my", consumes = "multipart/form-data")
    private ResponseEntity<IResponse> editProfileWithPhotoUpload(@RequestParam MultipartFile photo,
                                                                 @RequestParam String name, @RequestParam String email,
                                                                 @RequestParam(required = false) String password, @RequestParam int removePhoto) {
        IResponse result = authService.setProfile(photo, name, email, password, removePhoto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
