package com.authLab.jsrAuth.models;

import javax.persistence.*;

@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @ManyToOne
    private SiteUser originalPoster;
    private String body;

    public Post(SiteUser op, String body){
        this.originalPoster = op;
        this.body = body;
    }
}
