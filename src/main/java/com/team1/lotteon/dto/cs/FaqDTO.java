package com.team1.lotteon.dto.cs;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class FaqDTO extends ArticleDTO {
    private String type2;

}

