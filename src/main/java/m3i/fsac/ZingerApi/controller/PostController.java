package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Comment;
import m3i.fsac.ZingerApi.model.Post;
import m3i.fsac.ZingerApi.model.Report;
import m3i.fsac.ZingerApi.model.User;
import m3i.fsac.ZingerApi.repository.CommentRepository;
import m3i.fsac.ZingerApi.repository.PostRepository;
import m3i.fsac.ZingerApi.repository.ReportRepository;
import m3i.fsac.ZingerApi.repository.UserRepository;
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
        //post.setIsBlocked(false);
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
            //postToUpdate.setIsBlocked(post.getIsBlocked() != null ? post.getIsBlocked() : postToUpdate.getIsBlocked());
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
                Optional<User> userTodDelete = userRepository.findByIdPosts(id);
                userTodDelete.get().getIdPosts().removeIf(ids -> ids.equals(id));

                //delete post from comment
                List<Comment> commentToDelete = commentRepository.findByPostId(id);
                for (Comment comment : commentToDelete) {
                    commentRepository.deleteById(comment.getId());
                }

                //delete post from report
                List<Report> reportToDelete = reportRepository.findByPostId(id);
                for (Report report : reportToDelete) {
                    reportRepository.deleteById(report.getId());
                }

                //delete post
                postRepository.deleteById(id);

                Optional<Post> deletedPost = postRepository.findById(id);
                if (deletedPost.isPresent()) {
                    return new ResponseEntity<>("ZNG-23", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("ZNG-13", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("ZNG-202", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
