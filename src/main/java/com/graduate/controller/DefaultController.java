package com.graduate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DefaultController {

//    @RequestMapping(method = RequestMethod.GET, value = "/")
//    public String index() {
//        return "index";
//    }

    @GetMapping(value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/"; //делаем перенаправление
    }


    //temporary templates
    @RequestMapping(value = "log", method = RequestMethod.GET)
    public String getLoginForm() {
        return "loginForm";
    }
    @RequestMapping(value = "restore", method = RequestMethod.GET)
    public String restorePassword() {
        return "restore";
    }

}
