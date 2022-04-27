package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Report;
import m3i.fsac.ZingerApi.repository.ReportRepository;
import org.json.JSONObject;
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

    @GetMapping("/report/get")
    public ResponseEntity<?> getReports() {
        List<Report> reports = reportRepository.findAll();
        if (reports.size() > 0) {
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            return new ResponseEntity<>(jo.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/report/get/{id}")
    public ResponseEntity<?> getReport(@PathVariable String id) {
        Optional<Report> reportOptional = reportRepository.findById(id);
        if (reportOptional.isPresent()) {
            return new ResponseEntity<>(reportOptional, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found  with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/report/add")
    public ResponseEntity<?> createReport(@RequestBody Report report) {
        try {
            report.setReportAt(new Date(System.currentTimeMillis()));

            reportRepository.save(report);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(reportToUpdate, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/report/delete/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable String id) {
        Optional<Report> reportOptional = reportRepository.findById(id);
        if (reportOptional.isPresent()) {
            reportRepository.deleteById(id);
            Optional<Report> deletedReport = reportRepository.findById(id);
            if (deletedReport.isPresent()) {
                return new ResponseEntity<>("ZNG-25", HttpStatus.FAILED_DEPENDENCY);
            } else {
                return new ResponseEntity<>("ZNG-15", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }
}
