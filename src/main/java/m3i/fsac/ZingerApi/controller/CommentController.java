package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Comment;
import m3i.fsac.ZingerApi.repository.CommentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/comment/get")
    public ResponseEntity<?> getComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.size() > 0) {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            return new ResponseEntity<>(jo.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comment/get/{id}")
    public ResponseEntity<?> getComment(@PathVariable String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            return new ResponseEntity<>(commentOptional, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found  with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/comment/add")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        comment.setCreatedAt(new Date(System.currentTimeMillis()));

        commentRepository.save(comment);

        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        if (commentOptional.isPresent()) {
            return new ResponseEntity<>("ZNG-13", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-23", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/edit/{id}")
    public ResponseEntity<?> updateComment(@PathVariable String id, @RequestBody Comment comment) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment commentToUpdate = commentOptional.get();
            commentToUpdate.setBody(comment.getBody() != null ? comment.getBody() : commentToUpdate.getBody());
            commentToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            commentToUpdate.setCreatedAt(comment.getCreatedAt() != null ? comment.getCreatedAt() : commentToUpdate.getCreatedAt());

            commentRepository.save(commentToUpdate);
            return new ResponseEntity<>(commentToUpdate, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            commentRepository.deleteById(id);
            Optional<Comment> deletedComment = commentRepository.findById(id);
            if (deletedComment.isPresent()) {
                return new ResponseEntity<>("ZNG-25", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("ZNG-15", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }
}
