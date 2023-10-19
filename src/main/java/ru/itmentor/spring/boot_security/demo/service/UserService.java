package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.User;
import java.util.*;

public interface UserService {
    public List<User> findAll();
    public User findOne (long id);
    public void save(User user);
    public void update(Long id, User updateUser);
    public void delete(Long id);
    public Optional<User> findByUsername(String name);
}