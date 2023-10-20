package ru.itmentor.spring.boot_security.demo.pojo;

import ru.itmentor.spring.boot_security.demo.model.Role;

import java.util.Set;

public class SignupRequest {
    private String username;
    private String lastName;
    private Integer Age;
    private String email;
    private Set<String> roles;
    private String password;

    public SignupRequest() {}

    public SignupRequest(String username, String lastName, Integer age,
                         String email, Set<String> roles, String password) {
        this.username = username;
        this.lastName = lastName;
        Age = age;
        this.email = email;
        this.roles = roles;
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
