package day27workshop.day27workshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private String user;

    private Integer rating;

    private String comment;

    private Integer id;

    // private LocalDateTime posted;
    
    // private String boardGameName;

    // public Review(String user, String rating, String comment, Integer id) {
    //     this.user = user;
    //     this.rating = rating;
    //     this.comment = comment;
    //     this.id = id;
    //     // this.posted = LocalDateTime.now();
    //     // this.boardGameName = boardGameName;
    // }

    
    
}
