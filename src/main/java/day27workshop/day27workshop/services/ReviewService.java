package day27workshop.day27workshop.services;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import day27workshop.day27workshop.model.Review;
import day27workshop.day27workshop.repositories.ReviewRepository;
import jakarta.json.JsonObject;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepo;
    

    public Review insertReview(Review review) {
        reviewRepo.insertReview(review);
        return review;
    }

    public Document insertDocReview(Review review) {
        Document task = reviewRepo.insertDocReview(review);
        return task;
    }

    public JsonObject updateReview(String reviewId, Review review) {
        JsonObject response = reviewRepo.updateReview(reviewId, review);
        return response;
    }

    public JsonObject getLatestReview(String id){
        JsonObject response = reviewRepo.getLatestReview(id);
        return response;
    }

    public JsonObject   getReviewHistory(String id){
        JsonObject response = reviewRepo.getReviewHistory(id);
        return response;
    }
    
}
