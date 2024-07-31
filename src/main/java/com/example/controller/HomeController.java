package com.example.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/home")
@CrossOrigin
public class HomeController {

    @GetMapping
    public ResponseEntity<ResponseMessage> home() {
        ResponseMessage response = new ResponseMessage("Welcome to the Home page!");
        return ResponseEntity.ok(response);
    }

    // Classe interne pour représenter le message de réponse
    public static class ResponseMessage {
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
