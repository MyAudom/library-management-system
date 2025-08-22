package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.entity.Member;
import com.example.librarymanagementsystem.repository.MemberRepository;
import com.example.librarymanagementsystem.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoanRepository loanRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        // ពិនិត្យមើលថាតើសមាជិកនេះមានការខ្ចីដែរឬទេ (ទាំង active និង returned loans)
        long totalLoansCount = loanRepository.countByBookId(id); // ប្រើ countByMemberId ជំនួស
        if (totalLoansCount > 0) {
            // ពិនិត្យថាមានការខ្ចីដែលមិនទាន់ត្រលប់ដែរឬទេ
            long activeLoansCount = loanRepository.countByMemberIdAndReturnDateIsNull(id);
            if (activeLoansCount > 0) {
                throw new IllegalStateException("Cannot delete member. This member has " + activeLoansCount +
                        " active loan(s) that haven't been returned yet.");
            } else {
                // មានតែ loan records ដែលត្រលប់រួចហើយ
                long returnedLoansCount = loanRepository.countByMemberIdAndReturnDateIsNotNull(id);
                throw new IllegalStateException("Cannot delete member. This member has loan history with " +
                        returnedLoansCount + " returned book(s). Data preservation required.");
            }
        }

        // បើគ្មាន loan records ទាំងអស់ទើបអាចលុបបាន
        memberRepository.deleteById(id);
    }

    // Method ថ្មីសម្រាប់ជួយ count loans correctly
    public long getTotalLoansForMember(Long memberId) {
        // ត្រូវការ method ថ្មីនៅ LoanRepository
        return loanRepository.countByMemberId(memberId);
    }

    public long getActiveLoansForMember(Long memberId) {
        return loanRepository.countByMemberIdAndReturnDateIsNull(memberId);
    }

    public long getReturnedLoansForMember(Long memberId) {
        return loanRepository.countByMemberIdAndReturnDateIsNotNull(memberId);
    }
}