package com.team1.lotteon.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class FaqDTO extends ArticleDTO{
    private String type2;

}

