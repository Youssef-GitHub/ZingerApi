package m3i.fsac.ZingerApi.repository;

import m3i.fsac.ZingerApi.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    public List<Comment> findByUserId(String id);
}
