package com.team1.lotteon.dto.order;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageDTO {
    private String message;
    private boolean success;
}
