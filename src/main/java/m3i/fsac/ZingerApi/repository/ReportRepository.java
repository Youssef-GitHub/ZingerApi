package m3i.fsac.ZingerApi.repository;

import m3i.fsac.ZingerApi.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
}
