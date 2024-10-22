package com.team1.lotteon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
public class NoticeDTO extends ArticleDTO{
}
