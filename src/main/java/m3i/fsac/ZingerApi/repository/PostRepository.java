package m3i.fsac.ZingerApi.repository;

import m3i.fsac.ZingerApi.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    public List<Post> findByUserId(String id);
    public Optional<Post> findByIdComments(String id);
    public Optional<Post> findByIdReports(String id);
}
