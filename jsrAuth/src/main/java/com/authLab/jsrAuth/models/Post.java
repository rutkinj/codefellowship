package com.authLab.jsrAuth.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne
    private SiteUser originalPoster;
    private String body;

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    private LocalDateTime timeStamp = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public SiteUser getOriginalPoster() {
        return originalPoster;
    }

    public void setOriginalPoster(SiteUser originalPoster) {
        this.originalPoster = originalPoster;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    protected Post(){}
    public Post(SiteUser op, String body){
        this.originalPoster = op;
        this.body = body;
    }
}
