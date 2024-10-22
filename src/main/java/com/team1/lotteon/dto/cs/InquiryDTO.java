package com.team1.lotteon.dto.cs;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class InquiryDTO extends ArticleDTO {
    private String type2;
    private String answer;
}
