package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.Loan;
import com.example.librarymanagementsystem.entity.Member;
import com.example.librarymanagementsystem.service.BookService;
import com.example.librarymanagementsystem.service.LoanService;
import com.example.librarymanagementsystem.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/library")
public class ApiController {
    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private LoanService loanService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/members")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/loans")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/active-loans-count")
    public long getActiveLoansCount() {
        return loanService.getActiveLoansCount();
    }

    @GetMapping("/done-active-loans-count")
    public long getDoneActiveLoansCount() {
        return loanService.getDoneActiveLoansCount();
    }
}