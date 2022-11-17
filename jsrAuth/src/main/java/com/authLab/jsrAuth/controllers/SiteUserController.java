package com.authLab.jsrAuth.controllers;

import com.authLab.jsrAuth.models.Post;
import com.authLab.jsrAuth.models.SiteUser;
import com.authLab.jsrAuth.repositories.PostRepository;
import com.authLab.jsrAuth.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class SiteUserController {

    @Autowired
    SiteUserRepository siteUserRepository;

    @Autowired
    PostRepository postRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/")
    public String getHome(Model m, Principal p){
        if(p != null){
            String username = p.getName();
            SiteUser foundUser = siteUserRepository.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", foundUser.getFirstName());
            m.addAttribute("lastName", foundUser.getLastName());
        }
        return "index";
    }

    // log in
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    // sign up
    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password, String firstName, String lastName){
        String hashedPass = passwordEncoder.encode(password);
        SiteUser newUser = new SiteUser(username, hashedPass, firstName, lastName);
        siteUserRepository.save(newUser);
        authWithHttpServletRequest(username, password);
        return new RedirectView("/myprofile");
    }

    public void authWithHttpServletRequest(String username, String password){
        try {
            request.login(username, password);
        } catch (ServletException e){
            e.printStackTrace();
        }
    }

    // log out
    @GetMapping("/sauce")
    public String getSauce(){
        return "secret";
    }

    @GetMapping("/myprofile")
    public RedirectView getMyProfile(Principal p){
        Long myId = siteUserRepository.findByUsername(p.getName()).getId();

        return new RedirectView("/users/" + myId);
    }

    @GetMapping("users/{id}")
    public String getUserInfo(Model m, Principal p, @PathVariable Long id){
        SiteUser authenticatedUser = siteUserRepository.findByUsername(p.getName());
        m.addAttribute("authenticatedUsername", authenticatedUser.getUsername());

        SiteUser viewUser = siteUserRepository.findById(id).orElseThrow();
        m.addAttribute("viewUsername", viewUser.getUsername());
        m.addAttribute("viewUserId", viewUser.getId());
        m.addAttribute("viewFirstName", viewUser.getFirstName());
        m.addAttribute("viewLastName", viewUser.getLastName());
        m.addAttribute("posts", viewUser.getPostList());

        return "user-info";
    }

    @PutMapping("users/{id}")
    public RedirectView editUserInfo(Model m, Principal p, @PathVariable Long id, String username, RedirectAttributes redirect) throws ServletException{
        SiteUser userToEdit = siteUserRepository.findById(id).orElseThrow();
        if (p.getName().equals(userToEdit.getUsername())){
            userToEdit.setUsername(username);
            siteUserRepository.save(userToEdit);
            request.logout();
        } else {
            redirect.addFlashAttribute("errorMsg", "You lack authority");
        }
        return new RedirectView("/logout");
    }

    @PostMapping("/post")
    public RedirectView addNewPost(String postUserId, String body){
        SiteUser op = siteUserRepository.findByUsername(postUserId);
        Post newPost = new Post(op, body);
        postRepository.save(newPost);

        return new RedirectView("/myprofile");
    }
}
