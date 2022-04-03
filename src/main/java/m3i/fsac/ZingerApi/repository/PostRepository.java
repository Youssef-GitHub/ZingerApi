package m3i.fsac.ZingerApi.repository;

import m3i.fsac.ZingerApi.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    public List<Post> findByUserId(String id);
}
