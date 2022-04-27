package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Post;
import m3i.fsac.ZingerApi.repository.CommentRepository;
import m3i.fsac.ZingerApi.repository.PostRepository;
import m3i.fsac.ZingerApi.repository.ReportRepository;
import m3i.fsac.ZingerApi.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/post/get")
    public ResponseEntity<?> getPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.size() > 0) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-202", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/post/get/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            return new ResponseEntity<>(postOptional, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-202", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/post/add")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        post.setCreatedAt(new Date(System.currentTimeMillis()));
        post.setIsBlocked(false);
        post.setIdComments(new ArrayList<>());
        post.setReactions(new ArrayList<>());
        post.setIdReports(new ArrayList<>());

        postRepository.save(post);

        Optional<Post> postOptional = postRepository.findById(post.getId());
        if (postOptional.isPresent()) {
            return new ResponseEntity<>("ZNG-11", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-21", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/post/edit/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody Post post) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post postToUpdate = postOptional.get();
            postToUpdate.setType(post.getType() != null ? post.getType() : postToUpdate.getType());
            postToUpdate.setUrl(post.getUrl() != null ? post.getUrl() : postToUpdate.getUrl());
            postToUpdate.setBody(post.getBody() != null ? post.getBody() : postToUpdate.getBody());
            postToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            postToUpdate.setCreatedAt(post.getCreatedAt() != null ? post.getCreatedAt() : postToUpdate.getCreatedAt());
            postToUpdate.setIsBlocked(post.getIsBlocked() != null ? post.getIsBlocked() : postToUpdate.getIsBlocked());
            postToUpdate.setIdComments(post.getIdComments() != null ? post.getIdComments() : postToUpdate.getIdComments());
            postToUpdate.setIdReports(post.getIdReports() != null ? post.getIdReports() : postToUpdate.getIdReports());
            postToUpdate.setReactions(post.getReactions() != null ? post.getReactions() : postToUpdate.getReactions());

            postRepository.save(postToUpdate);

            if (Integer.toString(postOptional.hashCode()).toString().equals(Integer.toString(postOptional.hashCode()).toString())) {
                return new ResponseEntity<>("ZNG-12", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("ZNG-22", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("ZNG-202", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/post/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            Optional<Post> postOptional = postRepository.findById(id);
            if (postOptional.isPresent()) {
                //delete post from user
                userRepository.findByIdPosts(id);

                //delete post from comment

                //delete post report

                //delete post

                //delete comments
                for (String comment : postOptional.get().getIdComments()) {
                    commentRepository.deleteById(comment);
                }

                //delete reports
                for (String report : postOptional.get().getIdReports()) {
                    reportRepository.deleteById(report);
                }
            } else {
                return new ResponseEntity<>("ZNG-202", HttpStatus.NOT_FOUND);
            }

            //delete post from user collection

            postRepository.deleteById(id);
            return new ResponseEntity<>("Deleted post with id = " + id, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
