insert into blog.users (email, is_moderator , name, password, reg_time) values
    ('mf93@mail.ru', 1, 'first', '1', '2021-01-01 18:34:11'),
    ('mail1@mail.ru', 0, 'second', '2', '2021-01-02 18:34:11'),
    ('mail2@mail.ru', 0, 'third', '3', '2021-01-03 18:34:11'),
    ('mail3@mail.ru', 1, '4th', '4', '2021-01-04 18:34:11');

insert into blog.posts (is_active, moderation_status, moderator_id, `text`, `time`, title, view_count, user_id) values
    (1, 'ACCEPTED', 1, '1', '2021-01-05 18:34:11', 'title1', 0, 1),
    (1, 'ACCEPTED', 1, '2', '2021-01-05 18:35:11', 'title2', 0, 1),
    (1, 'ACCEPTED', 1, '3', '2021-01-05 18:36:11', 'title3', 0, 1),
    (1, 'ACCEPTED', 1, '4', '2021-01-05 18:37:11', 'title4', 0, 3),
    (1, 'ACCEPTED', 1, '5', '2021-01-05 18:38:11', 'title5', 0, 2),
    (1, 'ACCEPTED', 1, '6', '2021-01-05 18:39:11', 'title6', 0, 2);

INSERT into blog.post_votes (user_id, post_id, value, `time`) values
    (1, 1, 1, '2021-01-06 18:34:11'),
    (1, 2, 1, '2021-01-06 18:34:11'),
    (1, 3, -1, '2021-01-06 18:34:11'),
    (1, 4, 1, '2021-01-06 18:34:11'),
    (2, 1, -1, '2021-01-06 18:34:11'),
    (2, 2, 1, '2021-01-06 18:34:11'),
    (2, 3, -1, '2021-01-06 18:34:11'),
    (3, 1, 1, '2021-01-06 18:34:11'),
    (4, 5, 1, '2021-01-06 18:34:11');

insert into blog.post_comments (user_id, post_id, `text`, `time`) values
    (1, 1, '17', '2021-01-07 18:34:11'),
    (2, 1, '17', '2021-01-07 18:34:11'),
    (3, 1, '17', '2021-01-07 18:34:11'),
    (4, 2, '17', '2021-01-07 18:34:11'),
    (1, 3, '17', '2021-01-07 18:34:11'),
    (2, 3, '17', '2021-01-07 18:34:11'),
    (3, 4, '17', '2021-01-07 18:34:11');

INSERT into blog.tags (name) values ('tag1'), ('tag2'), ('tag3'), ('tag4');

INSERT into blog.tag2post (post_id, tag_id) values (1, 1), (1, 2), (1, 3), (2, 1), (3, 2);