package com.graduate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DefaultController {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "log", method = RequestMethod.GET)
    public String getLoginForm() {//Model model) {
//        model.addAttribute("title", "Login form");
        return "loginForm";
    }


}
