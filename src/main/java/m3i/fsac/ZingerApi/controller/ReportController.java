package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Post;
import m3i.fsac.ZingerApi.model.Report;
import m3i.fsac.ZingerApi.repository.PostRepository;
import m3i.fsac.ZingerApi.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/report/get")
    public ResponseEntity<?> getReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.size() > 0) {
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-204", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/report/get/{id}")
    public ResponseEntity<?> getReport(@PathVariable String id) {
        Optional<Report> reportOptional = reportRepository.findById(id);
        if (reportOptional.isPresent()) {
            return new ResponseEntity<>(reportOptional, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-204", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/report/add")
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        report.setReportAt(new Date(System.currentTimeMillis()));

        reportRepository.save(report);

        Optional<Report> reportOptional = reportRepository.findById(report.getId());
        if (reportOptional.isPresent()) {
            return new ResponseEntity<>("ZNG-11", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-21", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/report/edit/{id}")
    public ResponseEntity<?> updateReport(@PathVariable String id, @RequestBody Report report) {
        Optional<Report> reportOptional = reportRepository.findById(id);
        if (reportOptional.isPresent()) {
            Report reportToUpdate = reportOptional.get();
            reportToUpdate.setBody(report.getBody() != null ? report.getBody() : reportToUpdate.getBody());
            reportToUpdate.setReportAt(report.getReportAt() != null ? report.getReportAt() : reportToUpdate.getReportAt());

            reportRepository.save(reportToUpdate);
            if (Integer.toString(reportOptional.hashCode()).toString().equals(Integer.toString(reportOptional.hashCode()).toString())) {
                return new ResponseEntity<>("ZNG-12", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("ZNG-22", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("ZNG-204", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/report/delete/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable String id) {
        try {
            Optional<Report> reportOptional = reportRepository.findById(id);
            if (reportOptional.isPresent()) {
                //delete report from post
                Optional<Post> postToDelete = postRepository.findByIdReports(id);
                postToDelete.get().getIdReports().removeIf(ids -> ids.equals(id));

                //delete report
                reportRepository.deleteById(id);

                Optional<Report> deletedReport = reportRepository.findById(id);
                if (deletedReport.isPresent()) {
                    return new ResponseEntity<>("ZNG-23", HttpStatus.FAILED_DEPENDENCY);
                } else {
                    return new ResponseEntity<>("ZNG-13", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("ZNG-204", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
