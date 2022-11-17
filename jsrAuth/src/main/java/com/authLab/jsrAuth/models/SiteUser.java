package com.authLab.jsrAuth.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class SiteUser implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "originalPoster")
    private Set<Post> postList;

    @ManyToMany
    @JoinTable(
            name = "followData",
            joinColumns = {@JoinColumn(name = "Follower")},
            inverseJoinColumns = {@JoinColumn(name = "Following")})
    Set<SiteUser> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    Set<SiteUser> followers = new HashSet<>();

    protected SiteUser(){};

    public SiteUser(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

//    GETTERS SETTERS OVERRIDERS


    public Set<SiteUser> getFollowing() {
        return following;
    }

    public Set<SiteUser> getFollowers() {
        return followers;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Post> getPostList() {
        return postList;
    }

    public void setPostList(Set<Post> postList) {
        this.postList = postList;
    }

}
