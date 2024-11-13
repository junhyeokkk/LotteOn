package com.team1.lotteon.dto.RequestDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BannerDeleteRequestDTO {
    private List<Long> ids;
}
