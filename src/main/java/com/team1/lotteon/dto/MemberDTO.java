package com.team1.lotteon.dto;

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
