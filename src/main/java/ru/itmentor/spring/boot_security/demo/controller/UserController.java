package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {
   private final UserService userService;

   @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> readAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable("id") int id) {
        return new ResponseEntity<>(userService.findOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody User user, @PathVariable("id") int id) {
        userService.update((long)id, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") int id) {
        userService.delete((long)id);
        return HttpStatus.OK;
    }
}