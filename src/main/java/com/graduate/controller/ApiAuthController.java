package com.graduate.controller;

import com.graduate.base.IResponse;
import com.graduate.request.ChangePassword;
import com.graduate.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth/")
public class ApiAuthController {

    private AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public IResponse checkAuth() {
        return authService.checkAuth();
    }


    // TODO: 14.02.2021 frontend code sending mime application/json
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public IResponse login(@RequestParam(value = "e_mail") String email, @RequestParam String password) {
        return authService.checkAuth();
    }


    // TODO: 14.02.2021 frontend code sending mime application/json
    @RequestMapping(value = "restore", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public IResponse restorePassword(@RequestParam(value = "e_mail") String email) {
        return authService.restorePassword(email);
    }

    @RequestMapping(value = "password", method = RequestMethod.POST, consumes = "application/json")
    public IResponse changePassword(@RequestBody ChangePassword changePassword) {
        return authService.changePassword(changePassword);
    }




}
