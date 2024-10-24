package com.team1.lotteon.dto;

import com.team1.lotteon.entity.GeneralMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
}
