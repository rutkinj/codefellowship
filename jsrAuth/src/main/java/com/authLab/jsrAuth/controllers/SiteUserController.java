package com.authLab.jsrAuth.controllers;

import com.authLab.jsrAuth.models.SiteUser;
import com.authLab.jsrAuth.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class SiteUserController {

    @Autowired
    SiteUserRepository siteUserRepository;

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
        return new RedirectView("/");
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
}
