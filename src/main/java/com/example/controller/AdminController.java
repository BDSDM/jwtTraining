package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("User deleted successfully"));
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("User not found"));
        }
    }

    @PostMapping("/user/{id}/addRole")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long id, @RequestBody String role) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRoles().add(role);
            userRepository.save(user);
            return ResponseEntity.ok(new ResponseMessage("Role added successfully"));
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("User not found"));
        }
    }

    @PostMapping("/user/{id}/removeRole")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long id, @RequestBody String role) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRoles().remove(role);
            userRepository.save(user);
            return ResponseEntity.ok(new ResponseMessage("Role removed successfully"));
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("User not found"));
        }
    }

    static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
