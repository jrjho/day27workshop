package day27workshop.day27workshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import day27workshop.day27workshop.model.Review;
import day27workshop.day27workshop.services.ReviewService;

@Controller
@RequestMapping()
public class ReviewController {

    @Autowired
    ReviewService reviewSvc;

    @PostMapping("/review")
    public String postReview(@RequestBody MultiValueMap<String, String> form, Model model) {

        String user = form.getFirst("user");
        String rating = form.getFirst("rating");
        String comment = form.getFirst("comment");
        Integer id = Integer.parseInt( form.getFirst("id"));
        // String boardGameName = form.getFirst("boardGameName");

        Review review = new Review(user, Integer.parseInt(rating), comment, id);
        Review insertedReview = reviewSvc.insertReview(review);    
        model.addAttribute("insertedReview", insertedReview);

        return "insertedreview";
    }
    
}
