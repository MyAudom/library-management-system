package com.example.librarymanagementsystem.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private int totalCopies;
    private int availableCopies;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "deleted_date")
    private LocalDate deletedDate; // Optional: for tracking deletions

    // Constructors
    public Book() {
        this.createdDate = LocalDate.now(); // Set creation date automatically
    }

    public Book(String title, String author, String isbn, int publicationYear, int totalCopies, int availableCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.createdDate = LocalDate.now();
    }

    // Existing getters and setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    // New getters and setters for timestamps
    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    public LocalDate getDeletedDate() { return deletedDate; }
    public void setDeletedDate(LocalDate deletedDate) { this.deletedDate = deletedDate; }
}