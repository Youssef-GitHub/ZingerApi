package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Comment;
import m3i.fsac.ZingerApi.model.Post;
import m3i.fsac.ZingerApi.model.Report;
import m3i.fsac.ZingerApi.repository.CommentRepository;
import m3i.fsac.ZingerApi.repository.PostRepository;
import m3i.fsac.ZingerApi.repository.ReportRepository;
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
    private CommentRepository commentRepository;
    private ReportRepository reportRepository;

    @GetMapping("/post/get")
    public ResponseEntity<?> getPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.size() > 0) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            return new ResponseEntity<>(jo.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/post/get/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            return new ResponseEntity<>(postOptional, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found  with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/post/add")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        try {
            post.setCreatedAt(new Date(System.currentTimeMillis()));
            post.setIsBlocked(false);
            post.setComments(new ArrayList<>());
            post.setReactions(new HashMap<>());
            post.setReports(new ArrayList<>());

            postRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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
            postToUpdate.setComments(post.getComments() != null ? post.getComments() : postToUpdate.getComments());
            postToUpdate.setReports(post.getReports() != null ? post.getReports() : postToUpdate.getReports());
            postToUpdate.setReactions(post.getReactions() != null ? post.getReactions() : postToUpdate.getReactions());

            postRepository.save(postToUpdate);
            return new ResponseEntity<>(postToUpdate, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/post/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            Optional<Post> postOptional = postRepository.findById(id);
            if (postOptional.isPresent()) {
                //delete comments
                for (Comment comment : postOptional.get().getComments()) {
                    commentRepository.deleteById(comment.getId());
                }

                //delete reports
                for (Report report : postOptional.get().getReports()) {
                    reportRepository.deleteById(report.getId());
                }
            } else {
                return new ResponseEntity<>("Post not found with id = " + id, HttpStatus.NOT_FOUND);
            }

            //delete post from user collection

            postRepository.deleteById(id);
            return new ResponseEntity<>("Deleted post with id = " + id, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
