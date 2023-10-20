package ru.itmentor.spring.boot_security.demo.pojo;

import ru.itmentor.spring.boot_security.demo.model.Role;

import java.util.Set;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String lastName;
    private Integer Age;
    private String email;
    private Set<Role> roles;

    public JwtResponse(String token, Long id, String username, String lastName,
                       Integer Age, String email, Set<Role> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.lastName = lastName;
        this.Age = Age;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}