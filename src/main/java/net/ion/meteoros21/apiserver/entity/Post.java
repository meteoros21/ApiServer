package net.ion.meteoros21.apiserver.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_post")
public class Post
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer postId;

    private String title;
    private String body;
    private String userId;

    @Column(name="reg_time", columnDefinition="DATETIME", insertable=false, updatable=false)
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime regTime;

    @ManyToOne(cascade= CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="userId", referencedColumnName="userId", insertable=false, updatable=false)
    private User user;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public String getRegDateString() {
        return this.regTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    public String getRegTimeString() {
        return this.regTime.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + this.regTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getUserId();
    }
}