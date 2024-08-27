package com.example.controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PreferenceController {

    // Endpoint pour sauvegarder la préférence de couleur dans un cookie
    @PostMapping("/api/set-color")
    public String setColorPreference(@RequestParam String color, HttpServletResponse response) {
        Cookie colorCookie = new Cookie("preferredColor", color);
        colorCookie.setMaxAge(20); // Durée de vie de 10 secondes
        colorCookie.setPath("/");
        colorCookie.setHttpOnly(true); // Sécuriser le cookie (si nécessaire)
        colorCookie.setSecure(true); // Assurez-vous que votre site utilise HTTPS
        response.addCookie(colorCookie);
        return "Couleur préférée sauvegardée: " + color;
    }


    // Endpoint pour lire la préférence de couleur à partir du cookie
    @GetMapping("/api/get-color")
    public String getColorPreference(@CookieValue(value = "preferredColor", defaultValue = "white") String preferredColor) {
        return preferredColor;
    }
}
