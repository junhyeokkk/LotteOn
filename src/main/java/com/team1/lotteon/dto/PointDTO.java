package com.team1.lotteon.dto;

import com.team1.lotteon.entity.GeneralMember;
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

    private int givePoints;  // 지급 포인트
    private int acPoints;   // 잔여 포인트

    private LocalDateTime createdat; // 지급날짜
    private String formattedCreatedAt; // 포맷된 날짜 필드

    // 외래키
    private String member_id;

    private GeneralMemberDTO member;

    public void setMember(GeneralMemberDTO member) {
        this.member = member;
        if (member != null) {
            this.member_id = member.getUid(); // 매핑되는 UID를 member_id에 설정
        }
    }

    public PointDTO(GeneralMemberDTO member, String type, int givePoints, int acPoints, LocalDateTime createdat) {
        this.member = member;
        this.type = type;
        this.givePoints = givePoints;
        this.acPoints = acPoints;
        this.createdat = createdat;
        if (member != null) {
            this.member_id = member.getUid();
        }
    }
}