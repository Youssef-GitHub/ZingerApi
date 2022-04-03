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
@Document(collection = "Report")
public class Report {
    @Id
    private String id;
    private String userId;
    private String postId;
    private String body;
    private Date reportAt;
}
