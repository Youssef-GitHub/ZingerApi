package m3i.fsac.ZingerApi.repository;

import m3i.fsac.ZingerApi.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {
    public List<Report> findByUserId(String id);
    public List<Report> findByPostId(String id);
}
