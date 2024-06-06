package com.library.model;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;

    public Book(String isbn, String title, String author, String category, boolean isAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    // Getters and setters
}
