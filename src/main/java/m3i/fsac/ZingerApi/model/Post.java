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
@Document(collection = "Post")
public class Post {
    @Id
    private String id;
    private String userId;
    private String type;
    private String body;
    private String url;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isBlocked;
    private List<String> idComments;
    private List<String> idReports;
    private List<String> reactions;
}
