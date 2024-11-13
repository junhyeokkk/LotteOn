package com.team1.lotteon.dto;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
/*
    날짜 : 2024/10/24
    이름 : 최준혁
    내용 : Point DTO 생성
*/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointDTO {

    private Long id;
    private String type;    // 지급내용


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // 구분 (예: "적립", "사용", "만료")

    private int givePoints;  // 지급 포인트
    private int acPoints;   // 잔여 포인트


    private LocalDateTime createdat; // 지급날짜
    private String formattedCreatedAt; // 포맷된 날짜 필드


    private LocalDateTime expirationDate; // 유효기간 필드 추가
    private String formattedExpirationDate; // 포맷된 유효기간 표시


    // 외래키
    private String member_id;

    private Order order;

    private GeneralMemberDTO member;

    public void setMember(GeneralMemberDTO member) {
        this.member = member;
        if (member != null) {
            this.member_id = member.getUid(); // 매핑되는 UID를 member_id에 설정
        }
    }


}