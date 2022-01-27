package com.example.urlshortener.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String email;
    private String password;
    private Role role;
    private Status status;
}