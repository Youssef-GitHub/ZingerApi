package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Comment;
import m3i.fsac.ZingerApi.model.Post;
import m3i.fsac.ZingerApi.model.User;
import m3i.fsac.ZingerApi.repository.CommentRepository;
import m3i.fsac.ZingerApi.repository.PostRepository;
import m3i.fsac.ZingerApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/comment/get")
    public ResponseEntity<?> getComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.size() > 0) {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-203", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comment/get/{id}")
    public ResponseEntity<?> getComment(@PathVariable String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            return new ResponseEntity<>(commentOptional, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-203", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/comment/add")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        comment.setCreatedAt(new Date(System.currentTimeMillis()));

        commentRepository.save(comment);

        Optional<Comment> commentOptional = commentRepository.findById(comment.getId());
        if (commentOptional.isPresent()) {
            return new ResponseEntity<>("ZNG-11", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-21", HttpStatus.NOT_FOUND);
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
            if (Integer.toString(commentOptional.hashCode()).toString().equals(Integer.toString(commentOptional.hashCode()).toString())) {
                return new ResponseEntity<>("ZNG-12", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("ZNG-22", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("ZNG-203", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            //delete comment from User
            Optional<User> userToDelete = userRepository.findByIdComments(id);
            userToDelete.get().getIdComments().removeIf(ids -> ids.equals(id));

            //delete comment from Post
            Optional<Post> postToDelete = postRepository.findByIdComments(id);
            postToDelete.get().getIdComments().removeIf(ids -> ids.equals(id));


            //delete comment
            commentRepository.deleteById(id);

            Optional<Comment> deletedComment = commentRepository.findById(id);
            if (deletedComment.isPresent()) {
                return new ResponseEntity<>("ZNG-23", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("ZNG-13", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("ZNG-203", HttpStatus.NOT_FOUND);
        }
    }
}
