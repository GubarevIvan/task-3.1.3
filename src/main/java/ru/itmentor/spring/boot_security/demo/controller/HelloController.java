package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.*;
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
    public List<String> printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("I'm Spring MVC application");
        messages.add("5.2.0 version by sep'19 ");
        return  messages;
    }
    @GetMapping(value = "user")
    @PreAuthorize("hasRole('USER') or hasAnyRole('ADMIN')")
    public String user(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user =  userService.findByUsername(name);

        return user.get().toString();
    }

//    @PatchMapping("/user")
//    public String addAdmin() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user =  userService.findByUsername(name).get();
//        Role role = roleService.findByUserRole(ERole.ROLE_USER).get();
//        Set<Role> roles = user.getRoles();
//        roles.add(role);
//        user.setRoles(roles);
//        return "redirect:/auth/admin";
//    }
}