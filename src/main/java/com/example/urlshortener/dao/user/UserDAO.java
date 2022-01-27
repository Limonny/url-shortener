package com.example.urlshortener.dao.user;

import com.example.urlshortener.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO {

    Optional<User> findByEmail(String email);
}