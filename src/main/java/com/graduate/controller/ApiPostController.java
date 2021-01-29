package com.graduate.controller;

import com.graduate.model.RepositoryConnector;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/api/post")
public class ApiPostController {


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPostsByMode(@RequestParam int offset,
                                                 @RequestParam int limit, @RequestParam String mode) {
        System.err.println(offset +"\t"+ limit +"\t"+ mode);
        String result = RepositoryConnector.getPostsByMode(offset, limit, mode);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPostsBySearch(@RequestParam int offset,
                                                   @RequestParam int limit, @RequestParam String query) {
        String result = RepositoryConnector.getPostsBySearch(offset, limit, query);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/byDate", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPostsByDate(@RequestParam int offset,
                                                 @RequestParam int limit, @RequestParam String date) {
        String result = RepositoryConnector.getPostsByDate(offset, limit, date);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/byTag", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPostsByTag(@RequestParam int offset,
                                                @RequestParam int limit, @RequestParam String tag) {
        String result = RepositoryConnector.getPostsByTag(offset, limit, tag);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPostsByModerationStatus(@RequestParam int offset,
                                                             @RequestParam int limit, @RequestParam String status) {
        // TODO: 17.01.2021 implement receiving moderator_id (is the current user a moderator?)
        String result = RepositoryConnector.getPostsByModerationStatus(offset, limit, status);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(result);
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getMyPosts(@RequestParam int offset,
                                             @RequestParam int limit, @RequestParam String status) {
        // TODO: 17.01.2021 implement receiving user_id (is the user has auth)
        String result = RepositoryConnector.getMyPosts(offset, limit, status);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getPostById(@PathVariable int id) {
        String result = RepositoryConnector.getPostById(id);
        return result.length() > 0 ? ResponseEntity.status(HttpStatus.OK).body(result) :
                                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }








    /*
    insert into blog.users (email, is_moderator , name, password, reg_time) values
    ('mail@mail.ru', 1, 'first', '1', '2021-01-01 18:34:11.935142000'),
    ('mail@mail.ru', 2, 'first', '1', '2021-01-02 18:34:11.935142000'),
    ('mail@mail.ru', 3, 'first', '1', '2021-01-03 18:34:11.935142000'),
    ('mail@mail.ru', 4, 'first', '1', '2021-01-04 18:34:11.935142000')

    insert into blog.posts (is_active, moderation_status, moderator_id, `text`, `time`, title, view_count, user_id) values
    (0, 'NEW', 1, '1', '2021-01-05 18:34:11.935142000', 'title1', 0, 1),
    (0, 'NEW', 1, '2', '2021-01-05 18:35:11.935142000', 'title2', 0, 1),
    (0, 'NEW', 1, '3', '2021-01-05 18:36:11.935142000', 'title3', 0, 1),
    (0, 'NEW', 1, '4', '2021-01-05 18:37:11.935142000', 'title4', 0, 3),
    (0, 'NEW', 1, '5', '2021-01-05 18:38:11.935142000', 'title5', 0, 2),
    (0, 'NEW', 1, '6', '2021-01-05 18:39:11.935142000', 'title6', 0, 2)

    INSERT into blog.post_votes (user_id, post_id, value, `time`) values
    (1, 1, 1, '2021-01-06 18:34:11.935142000'),
    (1, 2, 1, '2021-01-06 18:34:11.935142000'),
    (1, 3, -1, '2021-01-06 18:34:11.935142000'),
    (1, 4, 1, '2021-01-06 18:34:11.935142000'),
    (2, 1, -1, '2021-01-06 18:34:11.935142000'),
    (2, 2, 1, '2021-01-06 18:34:11.935142000'),
    (2, 3, -1, '2021-01-06 18:34:11.935142000'),
    (3, 1, 1, '2021-01-06 18:34:11.935142000'),
    (4, 5, 1, '2021-01-06 18:34:11.935142000')

    insert into blog.post_comments (user_id, post_id, `text`, `time`) values
    (1, 1, '17', '2021-01-07 18:34:11.935142000'),
    (2, 1, '17', '2021-01-07 18:34:11.935142000'),
    (3, 1, '17', '2021-01-07 18:34:11.935142000'),
    (4, 2, '17', '2021-01-07 18:34:11.935142000'),
    (1, 3, '17', '2021-01-07 18:34:11.935142000'),
    (2, 3, '17', '2021-01-07 18:34:11.935142000'),
    (3, 4, '17', '2021-01-07 18:34:11.935142000')

    INSERT into blog.tags (name) values ('tag1'),('tag2'),('tag3'),('tag4')

    INSERT into blog.tag2post (post_id, tag_id) values (1, 1), (1, 2), (1, 3), (2, 1), (3, 2)
     */


}
