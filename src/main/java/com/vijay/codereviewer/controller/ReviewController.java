package com.vijay.codereviewer.controller;

import com.vijay.codereviewer.service.CodeReviewService;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final CodeReviewService reviewService;

    public ReviewController(CodeReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/code")
    public String review(@RequestBody String code) {

        return reviewService.review(code);
    }
    
    @GetMapping("/say")
    public String say() {
    	return "Hello";
    }
}