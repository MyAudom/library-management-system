package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void deleteBook(Long id) {
        // ពិនិត្យមើលថាតើមានការខ្ចីណាមួយសម្រាប់សៀវភៅនេះដែរទេ (ទាំងដែលត្រលប់រួច និងមិនទាន់ត្រលប់)
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new IllegalStateException("Book not found.");
        }

        Book book = bookOptional.get();

        // រាប់ការខ្ចីទាំងអស់ (active និង returned) សម្រាប់សៀវភៅនេះ
        long totalLoansCount = loanRepository.countByBookId(id);
        if (totalLoansCount > 0) {
            throw new IllegalStateException("Cannot delete book '" + book.getTitle() + "'. This book has loan history and cannot be deleted.");
        }

        // បើគ្មានការខ្ចីទេ ទើបអាចលុបបាន
        bookRepository.deleteById(id);
    }

    public void saveBook(Book book) {
        // Validation: Ensure availableCopies <= totalCopies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException("Available copies cannot exceed total copies.");
        }
        if (book.getTotalCopies() < 0 || book.getAvailableCopies() < 0) {
            throw new IllegalArgumentException("Copies cannot be negative.");
        }
        bookRepository.save(book);
    }
}