drop table if exists captcha_codes;
drop table if exists global_settings;
drop table if exists post_comments;
drop table if exists post_votes;
drop table if exists posts;
drop table if exists tag2post;
drop table if exists tags;
drop table if exists users;


create table captcha_codes (
    id integer not null auto_increment,
    code TINYTEXT not null,
    secret_code TINYTEXT not null,
    time datetime(6) not null,
    primary key (id)
)ENGINE=InnoDB;

create table global_settings (
    id integer not null auto_increment,
    code varchar(255) not null,
    name varchar(255) not null,
    value varchar(255) not null,
    primary key (id)
)ENGINE=InnoDB;

create table post_comments (
    id integer not null auto_increment,
    parent_id integer,
    text TEXT not null,
    time datetime(6) not null,
    post_id integer not null,
    user_id integer not null,
    primary key (id)
)ENGINE=InnoDB;

create table post_votes (
    id integer not null auto_increment,
    time datetime(6) not null,
    user_id integer not null,
    value tinyint not null,
    post_id integer not null,
    primary key (id)
)ENGINE=InnoDB;

create table posts (
    id integer not null auto_increment,
    is_active tinyint not null,
    moderation_status enum('NEW', 'ACCEPTED', 'DECLINED') not null,
    moderator_id integer,
    text varchar(255) not null,
    time datetime(6) not null,
    title varchar(255) not null,
    view_count integer not null,
    user_id integer,
    primary key (id)
)ENGINE=InnoDB;

create table tag2post (
    id integer not null auto_increment,
    post_id integer not null,
    tag_id integer not null,
    primary key (id)
)ENGINE=InnoDB;

create table tags (
    id integer not null auto_increment,
    name varchar(255) not null,
    primary key (id)
)ENGINE=InnoDB;

create table users (
    id integer not null auto_increment,
    code varchar(255),
    email varchar(255) not null,
    is_moderator tinyint not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo varchar(255),
    reg_time datetime(6) not null,
    primary key (id)
)ENGINE=InnoDB;

alter table post_comments add constraint FKaawaqxjs3br8dw5v90w7uu514 foreign key (post_id) references posts (id);
alter table post_comments add constraint FKsnxoecngu89u3fh4wdrgf0f2g foreign key (user_id) references users (id);
alter table post_votes add constraint FK9jh5u17tmu1g7xnlxa77ilo3u foreign key (post_id) references posts (id);
alter table posts add constraint FK5lidm6cqbc7u4xhqpxm898qme foreign key (user_id) references users (id);
alter table tag2post add constraint FKjou6suf2w810t2u3l96uasw3r foreign key (tag_id) references tags (id);
alter table tag2post add constraint FKpjoedhh4h917xf25el3odq20i foreign key (post_id) references posts (id);
