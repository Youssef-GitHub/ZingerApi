package m3i.fsac.ZingerApi.controller;

import m3i.fsac.ZingerApi.model.Post;
import m3i.fsac.ZingerApi.model.User;
import m3i.fsac.ZingerApi.repository.CommentRepository;
import m3i.fsac.ZingerApi.repository.PostRepository;
import m3i.fsac.ZingerApi.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    @GetMapping("/user/get")
    public ResponseEntity<?> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.size() > 0) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            return new ResponseEntity<>(jo.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/get/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional, HttpStatus.OK);
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found  with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        user.setIsBlocked(false);
        user.setIsAdmin(false);
        user.setPosts(new ArrayList<>(postRepository.findByUserId(user.getId())));
        user.setComments(new ArrayList<>(commentRepository.findByUserId(user.getId())));

        userRepository.save(user);

        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            return new ResponseEntity<>("ZNG-13", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ZNG-23", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/edit/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            userToUpdate.setFirst_name(user.getFirst_name() != null ? user.getFirst_name() : userToUpdate.getFirst_name());
            userToUpdate.setLast_name(user.getLast_name() != null ? user.getLast_name() : userToUpdate.getLast_name());
            userToUpdate.setEmail(user.getEmail() != null ? user.getEmail() : userToUpdate.getEmail());
            userToUpdate.setPassword(user.getPassword() != null ? user.getPassword() : userToUpdate.getPassword());
            userToUpdate.setBio(user.getBio() != null ? user.getBio() : userToUpdate.getBio());
            userToUpdate.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt() : userToUpdate.getCreatedAt());
            userToUpdate.setIsBlocked(user.getIsBlocked() != null ? user.getIsBlocked() : userToUpdate.getIsBlocked());
            userToUpdate.setIsAdmin(user.getIsAdmin() != null ? user.getIsAdmin() : userToUpdate.getIsAdmin());
            user.setPosts(user.getPosts() != null ? user.getPosts() : userToUpdate.getPosts());
            user.setComments(user.getComments() != null ? user.getComments() : userToUpdate.getComments());

            userRepository.save(userToUpdate);

            if (Integer.toString(userOptional.hashCode()).toString().equals(Integer.toString(userToUpdate.hashCode()).toString())) {
                return new ResponseEntity<>("ZNG-14", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("ZNG-24", HttpStatus.OK);
            }
        } else {
            JSONObject jo = new JSONObject();
            jo.put("not_found", "true");
            //"Post not found with id = " + id
            return new ResponseEntity<>("ZNG-22", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);

            return new ResponseEntity<>("Deleted post with id = " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/verify/{email}/{password}")
    public ResponseEntity<?> verifyUser(@PathVariable String email, @PathVariable String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            if (userOptional.get().getPassword().equals(password)) {
                return new ResponseEntity<>(userOptional, HttpStatus.OK);
            } else {
                JSONObject jo = new JSONObject();
                jo.put("password_invalid", "true");
                return new ResponseEntity<>("ZNG-21", HttpStatus.NOT_FOUND);
            }
        } else {
            JSONObject jo = new JSONObject();
            jo.put("email_not_found", "true");
            return new ResponseEntity<>("ZNG-20", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/verifyEmail/{email}")
    public ResponseEntity<?> verifyEmail(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        JSONObject jo = new JSONObject();
        if (userOptional.isPresent()) {
            jo.put("email_found", "true");
            return new ResponseEntity<>("ZNG-10", HttpStatus.OK);
        } else {
            jo.put("email_not_found", "true");
            return new ResponseEntity<>("ZNG-20", HttpStatus.NOT_FOUND);
        }
    }
}
