package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.*;
import ru.itmentor.spring.boot_security.demo.service.*;
import java.util.*;

@RestController
@RequestMapping("/")
public class HelloController {
    public final UserService userService;

    public final RoleService roleService;

    @Autowired
    public HelloController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public List<String> printWelcome() {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC application");
        messages.add("5.2.0 version by sep'19 ");
        return  messages;
    }
    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String user() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user =  userService.findByUsername(name);

        return user.get().toString();
    }
}