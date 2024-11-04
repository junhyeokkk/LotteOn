package com.team1.lotteon.security;

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
            model.addAttribute("isAuthenticated", true);
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities()
                    .toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace("ROLE_", "");
            log.info("role1111"+role);
            model.addAttribute("role", role);
            model.addAttribute("userDetails", userDetails);
            if(role.equals("General")){
                String uid = userDetails.getMember().getUid(); // 로그인한 uid
                String name = (userDetails.getGeneralMember() != null) ? userDetails.getGeneralMember().getName() : "No Name"; // GeneralMember의 이름
                model.addAttribute("uid", uid); // 로그인 uid
                model.addAttribute("name", name); // GeneralMember의 name
            }else if(role.equals("Seller")){
                String uid = userDetails.getMember().getUid();
                String name = (userDetails.getSellerMember() != null) ? userDetails.getSellerMember().getShop().getShopName() : "No Name"; // SellerMember의 상점이름
                model.addAttribute("uid", uid);
                model.addAttribute("name", name);
            }else if(role.equals("Admin")){
                String uid = userDetails.getMember().getUid();
//                String name = (userDetails.getMember() != null) ? userDetails.getMember().getName() : "No Name";
                String name = "관리자";
                model.addAttribute("uid", uid);
                model.addAttribute("name", name);
            }
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }
}

