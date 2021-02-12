package com.graduate.controller;

import com.graduate.base.IResponse;
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

    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
    public String login(@RequestParam(value = "e_mail") String email, @RequestParam String password) {
        System.err.println("signIn" + email +" "+ password);
        return "signIn";
    }



}
