package com.team1.lotteon.dto;

import com.team1.lotteon.entity.GeneralMember;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String uid;
    private String pass;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
