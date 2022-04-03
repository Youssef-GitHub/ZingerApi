package m3i.fsac.ZingerApi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class User {
    @Id
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String bio;
    private Date createdAt;
    private Boolean isBlocked;
    private Boolean isAdmin;
    private List<Post> posts;
    private List<Comment> comments;

}
