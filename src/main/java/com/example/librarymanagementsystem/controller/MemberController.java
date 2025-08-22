package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.entity.Member;
import com.example.librarymanagementsystem.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members";
    }

    @GetMapping("/new")
    public String showMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Member> optional = memberService.getMemberById(id);
        if (optional.isPresent()) {
            model.addAttribute("member", optional.get());
            return "member-form";
        } else {
            // member not found — redirect back to members list
            return "redirect:/members";
        }
    }

    @PostMapping("/save")
    public String saveMember(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {
        try {
            memberService.saveMember(member);
            redirectAttributes.addFlashAttribute("success", "Member saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving member: " + e.getMessage());
        }
        return "redirect:/members";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // ទាញយកព័ត៌មាន member មុននឹងលុប
            Optional<Member> memberOptional = memberService.getMemberById(id);
            if (memberOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Member not found!");
                return "redirect:/members";
            }

            Member member = memberOptional.get();
            memberService.deleteMember(id);
            redirectAttributes.addFlashAttribute("success", "Member \"" + member.getName() + "\" deleted successfully!");

        } catch (IllegalStateException e) {
            // កែសម្រួលសារ error ឲ្យស្អាតជាង
            String errorMessage = e.getMessage();
            if (errorMessage.contains("active loan")) {
                redirectAttributes.addFlashAttribute("error", "❌ " + errorMessage);
            } else if (errorMessage.contains("loan history")) {
                redirectAttributes.addFlashAttribute("error", "⚠️ " + errorMessage);
            } else {
                redirectAttributes.addFlashAttribute("error", "❌ " + errorMessage);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Unexpected error: " + e.getMessage());
        }
        return "redirect:/members";
    }

    // Method ថ្មីសម្រាប់បង្ហាញព័ត៌មាន loan របស់ member
    @GetMapping("/loans/{id}")
    public String viewMemberLoans(@PathVariable Long id, Model model) {
        Optional<Member> memberOptional = memberService.getMemberById(id);
        if (memberOptional.isEmpty()) {
            return "redirect:/members";
        }

        Member member = memberOptional.get();
        model.addAttribute("member", member);
        model.addAttribute("activeLoans", memberService.getActiveLoansForMember(id));
        model.addAttribute("returnedLoans", memberService.getReturnedLoansForMember(id));
        model.addAttribute("totalLoans", memberService.getTotalLoansForMember(id));

        return "member-loans"; // ត្រូវការ template ថ្មីនេះ
    }
}