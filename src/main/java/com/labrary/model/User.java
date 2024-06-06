package com.library.model;

import java.util.List;

public class User {
    private String userId;
    private String name;
    private List<String> borrowedBooks;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

}
