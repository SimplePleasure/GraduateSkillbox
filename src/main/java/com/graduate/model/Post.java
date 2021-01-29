package com.graduate.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "is_active")
    private byte isActive;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
    private ModerationStatus moderationStatus;

    // TODO: 10.01.2021 in db this field marks not null
    @Column(name = "moderator_id")
    private Integer moderatorId;
    @NotNull
    private LocalDateTime time;
    @NotNull
    private String title;
    @NotNull
    private String text;
    @NotNull
    @Column(name = "view_count")
    private int viewCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Tag2Post", joinColumns = {@JoinColumn(name = "post_id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    List<Tags> tags;

    // TODO: 12.01.2021 add synchronized methods
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostVotes> postVotes;
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostComments> postComments;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public byte getIsActive() {
        return isActive;
    }
    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }
    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }
    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    // TODO: 09.01.2021 add one to many synchronized methods!
    public List<PostVotes> getPostVotes() {
        return postVotes;
    }
    public void setPostVotes(List<PostVotes> postVotes) {
        this.postVotes = postVotes;
    }

    public List<PostComments> getPostComments() {
        return postComments;
    }
    public void setPostComments(List<PostComments> postComments) {
        this.postComments = postComments;
    }
}


