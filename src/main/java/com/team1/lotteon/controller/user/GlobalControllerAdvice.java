package com.team1.lotteon.controller.user;

import com.team1.lotteon.security.MyUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Log4j2
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAuthenticationToModel(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            String uid = userDetails.getMember().getUid(); // 로그인한 uid
            String name = (userDetails.getGeneralMember() != null) ? userDetails.getGeneralMember().getName() : "No Name"; // GeneralMember의 이름
            String role = userDetails.getAuthorities()
                    .toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace("ROLE_", "");
            log.info(role);
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("role", role);
            model.addAttribute("uid", uid); // 로그인 uid
            model.addAttribute("name", name); // GeneralMember의 name
            model.addAttribute("userDetails", userDetails);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }
}

