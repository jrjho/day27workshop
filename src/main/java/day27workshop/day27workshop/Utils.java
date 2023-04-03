package day27workshop.day27workshop;

import java.time.LocalDateTime;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

import day27workshop.day27workshop.exception.Exceptions;
import day27workshop.day27workshop.model.Review;

public class Utils {

    public static Review checkUserInput(MultiValueMap<String, String> form) {
        String user = form.getFirst("user");
        String rating = form.getFirst("rating");
        System.out.printf("Rating is: %s\n",rating);
        String comment = form.getFirst("comment");

        if (user == null || user.isBlank()) {
            throw new Exceptions("User is required");
        }

        if (rating == null || rating.isBlank()) {
            throw new Exceptions("Rating is required");
        }

        if (Integer.parseInt(rating) < 1 || Integer.parseInt(rating) > 10) {
            throw new Exceptions("Rating must be between 1 and 10");
        }

        try {
            String id = form.getFirst("id");
            System.out.printf("Id is: %s\n",id);

            if (id == null || id.isBlank()) {
                throw new Exceptions("Id is required");
            }
            Integer intGid = Integer.parseInt(id);
            Integer intRating = Integer.parseInt(rating);

            return new Review(user, intRating, comment, intGid);
        } catch (Exception e) {
            throw new Exceptions("Game id must be an integer");
        }

    }

    public static Document toDocument(Review review, String gameName) {

        Document document = new Document();
        document.append("user", review.getUser());
        document.append("rating", review.getRating());
        document.append("comment", review.getComment());
        document.append("ID", review.getId());
        document.append("posted", LocalDateTime.now().toString());
        document.append("name", gameName);

        System.out.printf("document: %s\n",document.toString());

        return document;
        
    }
}
