package m3i.fsac.ZingerApi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Comment")
public class Comment {
    @Id
    private String id;
    private String body;
    private String userId;
    private String postId;
    private Date createdAt;
    private Date updatedAt;
}
