package ru.itmentor.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.itmentor.spring.boot_security.demo.model.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select c from User c join fetch c.roles where c.username = :username")
    Optional<User> findByUsername(String username);
}