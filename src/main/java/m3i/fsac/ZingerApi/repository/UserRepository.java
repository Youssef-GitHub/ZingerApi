package m3i.fsac.ZingerApi.repository;

import m3i.fsac.ZingerApi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByIdPosts(String id);
}
