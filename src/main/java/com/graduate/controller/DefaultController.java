package com.graduate.controller;

import com.graduate.model.ModerationStatus;
import com.graduate.model.Post;
import com.graduate.model.RepositoryConnector;
import com.graduate.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class DefaultController {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/init", produces = "application/json")
    public String getMainInfo() {
        return "{\"title\":\"DevPub\",\"subtitle\":\"Рассказы разработчиков\",\"phone\":\"+7 903 666-44-55\",\"email\":\"mail@mail.ru\",\"copyright\":\"Дмитрий Сергеев\",\"copyrightFrom\":\"2005\"}";
    }





    private void t() {

        User user = new User();
        user.setIsModerator(Byte.parseByte("0"));
        user.setName("name");
        user.setEmail("test@mail.ru");
        user.setPassword("1111");
        user.setRegTime(LocalDateTime.now());
        RepositoryConnector.getUserRepository().save(user);

        Post p = new Post();
        p.setIsActive(Byte.parseByte("0"));
        p.setModerationStatus(ModerationStatus.NEW);
        p.setTitle("title");
        p.setText("text");
        p.setTime(LocalDateTime.now());
        p.setUser(user);
        p.setViewCount(0);
        RepositoryConnector.getPostRepository().save(p);
    }
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/fill")
    public String test() {
        t();
        return "";
    }



}
