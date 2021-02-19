package com.graduate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.graduate.base.IResponse;
import com.graduate.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {

    private PostService postService;

    public ApiPostController(@Autowired PostService postService) {
        this.postService = postService;
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
    public ResponseEntity<IResponse> getPostsByModerationStatus(@RequestParam int offset,
                                                             @RequestParam int limit, @RequestParam String status) {
        // TODO: 17.01.2021 implement receiving moderator_id (is the current user a moderator?)
        IResponse result = postService.getPostsByModerationStatus(offset, limit, status);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getMyPosts(@RequestParam int offset,
                                             @RequestParam int limit, @RequestParam String status) {
        // TODO: 17.01.2021 implement receiving user_id (is the user has auth)
        IResponse result = postService.getMyPosts(offset, limit, status);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<IResponse> getPostById(@PathVariable int id) {
        IResponse result = postService.getPostById(id);
        return result == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
