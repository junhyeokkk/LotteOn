package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.NoticeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends Article {
    @Enumerated(EnumType.STRING)
    private NoticeType type1;
}
