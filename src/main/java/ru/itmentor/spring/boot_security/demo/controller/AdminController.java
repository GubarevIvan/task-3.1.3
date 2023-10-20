package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {
    @ResponseBody
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage() {
        return "Вы на сранице Администратора сайта";
    }
}