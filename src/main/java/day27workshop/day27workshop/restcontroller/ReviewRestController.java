package day27workshop.day27workshop.restcontroller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import day27workshop.day27workshop.Utils;
import day27workshop.day27workshop.model.Review;
import day27workshop.day27workshop.services.ReviewService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api")
public class ReviewRestController {

    @Autowired
    ReviewService reviewSvc;

    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> insertReview(@RequestBody MultiValueMap<String, String> form) {

        try {
            Review review = Utils.checkUserInput(form);

            Document insertedReview = reviewSvc.insertDocReview(review);

            System.out.printf("username: %s\n",insertedReview.getString("user"));
            JsonObject response = Json.createObjectBuilder()
                    .add("user", insertedReview.getString("user"))
                    .add("rating", insertedReview.getInteger("rating"))
                    .add("comment", insertedReview.getString("comment"))
                    .add("ID", insertedReview.getInteger("ID"))
                    .add("posted", insertedReview.getString("posted"))
                    .add("name", insertedReview.getString("name"))    
                    .build();

            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            // return ResponseEntity.badRequest().body(e.getMessage());
            JsonObject err = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build();

            return ResponseEntity.status(400).body(err.toString());
        }

    }

    @PutMapping(path = "/review/{review_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReview( @PathVariable("review_id") String reviewId, @RequestBody Review review) {

        try{
            JsonObject response = reviewSvc.updateReview(reviewId, review);
            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            JsonObject err = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(400).body(err.toString());
        }
    }

    @GetMapping(path = "/review/{review_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLatestReview( @PathVariable("review_id") String reviewId) {

        try{
            JsonObject response = reviewSvc.getLatestReview(reviewId);
            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            JsonObject err = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(400).body(err.toString());
        }

    }

    @GetMapping(path = "/review/{review_id}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReviewHistory( @PathVariable("review_id") String reviewId) {

        try{
            JsonObject response = reviewSvc.getReviewHistory(reviewId);
            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            JsonObject err = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build();
            return ResponseEntity.status(400).body(err.toString());
        }

    }

}
