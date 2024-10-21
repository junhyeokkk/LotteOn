package com.team1.lotteon.apiController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class ProductController {
    @GetMapping("/login")
    public List<Object> getPRoduct() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    @PostMapping("/register")
    public void register() {

    }
}
