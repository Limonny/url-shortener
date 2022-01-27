package com.example.urlshortener.dao.user;

import com.example.urlshortener.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate template;

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        return template.query(sql, new UserEntityMapper(), email)
                .stream()
                .findFirst();
    }
}