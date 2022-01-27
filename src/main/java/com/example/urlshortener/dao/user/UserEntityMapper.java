package com.example.urlshortener.dao.user;

import com.example.urlshortener.model.user.Role;
import com.example.urlshortener.model.user.Status;
import com.example.urlshortener.model.user.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();

        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(Role.valueOf(rs.getString("role")));
        u.setStatus(Status.valueOf(rs.getString("status")));

        return u;
    }
}