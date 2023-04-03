package day27workshop.day27workshop.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import day27workshop.day27workshop.Utils;
import day27workshop.day27workshop.exception.Exceptions;
import day27workshop.day27workshop.model.Review;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Repository
public class ReviewRepository {

    private static final String C_GAMES = "game";

    // private static final String C_COMMENTS = "commment";

    private static final String C_REVIEWS = "review";

    @Autowired
    MongoTemplate mongoTemplate;

    // for normal controller
    public void insertReview(Review review) {
        mongoTemplate.insert(review, "reviews");
        System.out.printf(">>> Review inserted: %s", review.toString());
        // need to catch exception for duplicate keys
    }

    // for restcontroller
    public Document insertDocReview(Review review) {

        // get gid
        Integer gid = review.getId();
        Criteria criteria = Criteria.where("gid").is(gid);
        Query query = Query.query(criteria);

        List<Document> results = mongoTemplate.find(query, Document.class, C_GAMES);

        // check if results exists
        Document task = results.get(0);
        if (null == task) {

            throw new Exceptions("Game not found");
        }

        // call utils to create document using information from review and add name from
        // database
        Document insertReview = Utils.toDocument(review, task.getString("name"));
        insertReview = mongoTemplate.insert(insertReview, C_REVIEWS);
        System.out.printf("document: %s\n", task.getString("name"));

        return insertReview;
    }

    public JsonObject updateReview(String id, Review review) {

        try {
            System.out.printf("id is : %s", id);
            ObjectId oid = new ObjectId(id);
            Document existingReview = mongoTemplate.findById(oid, Document.class, C_REVIEWS);

            // check if review exists
            if (null == existingReview) {
                throw new Exceptions("Review not found");
            }
            // check if rating is between 1 and 10
            Integer rating = review.getRating();
            System.out.println("hello2");

            if (rating < 1 || rating > 10) {
                throw new Exceptions("Rating must be between 1 and 10");
            }

            JsonObject updated = Json.createObjectBuilder()
                    .add("comment", review.getComment())
                    .add("rating", review.getRating())
                    .add("posted", LocalDateTime.now().toString())
                    .build();

            Document toAddtoMongo = Document.parse(updated.toString());

            // update review
            Criteria criteria = Criteria.where("_id").is(oid);
            Query query = Query.query(criteria);
            Update update = new Update()
                    .set("rating", review.getRating())
                    .set("comment", review.getComment())
                    .push("edited", toAddtoMongo);

            mongoTemplate.updateFirst(query, update, getClass(), C_REVIEWS);

            return updated;

        } catch (Exception e) {
            throw new Exceptions("Not found!");
        }

    }

    public JsonObject getLatestReview(String id) {

        try {
            ObjectId oid = new ObjectId(id);
            Document existingReview = mongoTemplate.findById(oid, Document.class, C_REVIEWS);

            // check if review exists
            if (null == existingReview) {
                throw new Exceptions("Review not found");
            }
            // get latest review
            Boolean isEdited = false;
            List<Document> latestReview = existingReview.getList("edited", Document.class);
            System.out.printf("latestreview is: %s\n", latestReview);

            if (null == latestReview)
                isEdited = false;

            else
                isEdited = true;

            System.out.printf("isedited is: %s\n", isEdited);

            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("user", existingReview.getString("user"));
            builder.add("rating", existingReview.getString("rating"));
            builder.add("comment", existingReview.getString("comment"));
            builder.add("Game ID", existingReview.getInteger("ID"));
            builder.add("Game name", existingReview.getString("name"));
            builder.add("posted", existingReview.getString("posted"));

            if (isEdited) {
                // Document latest = latestReview.get(latestReview.size() - 1);
                builder.add("edited", isEdited);
                JsonObject latestReviewObj = builder.build();
                return latestReviewObj;

            }

            else {
                System.out.println("Hello1");

                // Document latest = latestReview.get(latestReview.size() - 1);
                builder.add("edited", isEdited);
                JsonObject latestReviewObj = builder.build();

                return latestReviewObj;

            }

        } catch (Exception e) {
            throw new Exceptions("Not found!");
        }

    }

    public JsonObject getReviewHistory(String id) {

        try {
            ObjectId oid = new ObjectId(id);
            Document existingReview = mongoTemplate.findById(oid, Document.class, C_REVIEWS);

            // check if review exists
            if (null == existingReview) {
                throw new Exceptions("Review not found");
            }
            // get latest review
            List<Document> editedReviews = existingReview.getList("edited", Document.class);
            System.out.println("hello1");
            // if (null == editedReviews) {
            // throw new Exceptions("Review has not been edited");
            // }

            // else {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            for (Document document : editedReviews) {

                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("comment", document.getString("comment"));
                jsonObjectBuilder.add("rating", document.getString("rating"));
                jsonObjectBuilder.add("posted", document.getString("posted"));
                jsonArrayBuilder.add(jsonObjectBuilder);

            }
            // }
            System.out.println("hello2");

            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("user", existingReview.getString("user"));
            builder.add("rating", existingReview.getString("rating"));
            builder.add("comment", existingReview.getString("comment"));
            builder.add("Game ID", existingReview.getInteger("ID"));
            builder.add("Game name", existingReview.getString("name"));
            builder.add("posted", existingReview.getString("posted"));
            builder.add("edited", jsonArrayBuilder);
            JsonObject latestReviewObj = builder.build();
            System.out.println("hello3");

            return latestReviewObj;

        } catch (Exception e) {
            throw new Exceptions("Review has not been edited!");
        }

    }

}
