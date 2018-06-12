package com.DeltaMutualFund.DeltaMutualFund.controller;

import com.DeltaMutualFund.DeltaMutualFund.service.AuthorityService;
import com.DeltaMutualFund.DeltaMutualFund.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private static final long ROLE_USER_AUTHORITY_ID = 2L;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root() {
        return "index";
    }


    @GetMapping("/index")
    public String index() {return "index";}


    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "Login failed, username or password is wrong!");
        return "index";
    }

    @GetMapping("/loginsuccess")
    public String loginSuccessful(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SimpleGrantedAuthority auth = (SimpleGrantedAuthority) userDetails.getAuthorities().toArray()[0];
        if (auth.getAuthority().equals("ROLE_ADMIN")) {
            return "redirect:/employee";
        } else if (auth.getAuthority().equals("ROLE_USER")) {
            return "redirect:/user";
        }
        return "index";
    }
}
