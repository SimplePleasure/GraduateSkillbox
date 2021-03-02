package com.graduate.controller;

import com.graduate.base.IResponse;
import com.graduate.request.ChangePassword;
import com.graduate.service.AuthService;
import com.graduate.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(value = "/api/auth")
public class ApiAuthController {

    private final AuthService authService;
    private final PostService postService;

    public ApiAuthController(@Autowired AuthService authService, @Autowired PostService postService) {
        this.authService = authService;
        this.postService = postService;
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public IResponse checkAuth(WebRequest request) {
        int postsCountWaitingModeration = request.isUserInRole("1") ? postService.getPostsCountWaitingModeration() : 0;
        return authService.checkAuth(postsCountWaitingModeration);
    }


    // TODO: 14.02.2021 frontend code sending mime application/json
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public void login(@RequestParam(value = "e_mail") String email, @RequestParam String password) {

    }


    // TODO: 14.02.2021 frontend code sending mime application/json
    @RequestMapping(value = "/restore", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public IResponse restorePassword(@RequestParam(value = "e_mail") String email) {
        return authService.restorePassword(email);
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST, consumes = "application/json")
    public IResponse changePassword(@RequestBody ChangePassword changePassword) {
        return authService.changePassword(changePassword);
    }




}
