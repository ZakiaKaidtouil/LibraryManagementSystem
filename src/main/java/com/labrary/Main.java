package com.library;

import com.library.model.Book;
import com.library.model.User;
import com.library.service.Library;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        // Adding books
        Book book1 = new Book("1234567890", "Java Programming", "Author A", "Programming", true);
        Book book2 = new Book("0987654321", "Database Systems", "Author B", "Databases", true);
        library.addBook(book1);
        library.addBook(book2);

        // Registering users
        User user1 = new User("u1", "John Doe");
        User user2 = new User("u2", "Jane Smith");
        library.registerUser(user1);
        library.registerUser(user2);

        // Borrowing a book
        library.borrowBook("u1", "1234567890");

        // Listing available books
        library.listAvailableBooks().forEach(book -> System.out.println(book.getTitle()));

        // Listing books borrowed by user
        library.listBooksBorrowedByUser("u1").forEach(book -> System.out.println(book.getTitle()));

        // Returning a book
        library.returnBook("u1", "1234567890");

        // Filtering books
        library.filterBooks("Java", "", "").forEach(book -> System.out.println(book.getTitle()));

        // Deleting a book
        library.deleteBook("0987654321");

        // Updating a book
        Book updatedBook = new Book("1234567890", "Advanced Java Programming", "Author A", "Programming", true);
        library.updateBook(updatedBook);
    }
}
