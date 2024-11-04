package com.team1.lotteon.service.MemberService;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.dto.ShopDTO;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.Memberrepository.MemberRepository;
import com.team1.lotteon.repository.Memberrepository.SellerMemberRepository;
import com.team1.lotteon.repository.shop.ShopRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final SellerMemberRepository sellerMemberRepository;
    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    public MemberDTO insertmember(MemberDTO memberDTO) {
        String encoded = passwordEncoder.encode(memberDTO.getPass());
        memberDTO.setPass(encoded);
        Member entity = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(entity);
        return null;
    }
    //일반 회원 등록
    @Transactional
    public GeneralMember insertGeneralMember(GeneralMemberDTO generalMemberDTO, MemberDTO memberDTO) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberDTO.getPass());
        // ModelMapper를 사용하여 DTO를 엔티티로 변환 (GeneralMember를 바로 변환)
        GeneralMember generalMember = modelMapper.map(generalMemberDTO, GeneralMember.class);
        generalMember.setPass(encodedPassword);
        generalMember.setRole("General");
        generalMember.setAddress(new Address(generalMemberDTO.getZip(), generalMemberDTO.getAddr1(), generalMemberDTO.getAddr2()));
        // GeneralMember 저장 시 자동으로 부모 클래스인 Member도 함께 저장
        return generalMemberRepository.save(generalMember);
    }

    //판매자 회원 등록
    @Transactional
    public void insertSellerMember(ShopDTO shopDTO, MemberDTO memberDTO) {
        // 1. SellerMember 생성 및 저장
        String encodedPassword = passwordEncoder.encode(memberDTO.getPass());
        SellerMember sellerMember = modelMapper.map(memberDTO, SellerMember.class);
        sellerMember.setPass(encodedPassword);
        sellerMember.setRole("Seller");

        // SellerMember 먼저 저장
        SellerMember savedSellerMember = sellerMemberRepository.save(sellerMember);

        // 2. Shop 생성 및 저장
        Shop shop = modelMapper.map(shopDTO, Shop.class);
        shop.setAddress(new Address(shopDTO.getZip(), shopDTO.getAddr1(), shopDTO.getAddr2()));

        // Shop 저장
        shopRepository.save(shop);

        // 3. SellerMember에 Shop 연관 설정 업데이트
        savedSellerMember.setShop(shop);
        sellerMemberRepository.save(savedSellerMember); // 연관 관계 업데이트 후 다시 저장
    }



    // 아이디 중복 확인
    public boolean isUidExist(String uid) {
        boolean check = memberRepository.existsByUid(uid);
        log.info(check);
        return check;
    }

    //이메일 인증 코드 전달
    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmailCode(HttpSession session, String receiver){


        // MimeMessage 생성
        MimeMessage message = mailSender.createMimeMessage();

        // 인증코드 생성 후 세션 저장
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        session.setAttribute("code", String.valueOf(code));
        log.info("인증코드 번호 : " + code);

        String title = "LotteOn1조 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            // 메일 발송
            mailSender.send(message);
        }catch(Exception e){
            log.error("sendEmailConde : " + e.getMessage());
        }
    }
//    2024/11/04 이도영 비밀번호 변경
    public boolean updatePassword(String uid, String encodedPassword) {
        Member member = memberRepository.findById(uid).orElse(null);
        if (member != null) {
            member.setPass(encodedPassword); // 비밀번호 업데이트
            memberRepository.save(member); // 변경 사항 저장
            return true;
        }
        return false;
    }
}
