package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.FaqType1;
import com.team1.lotteon.entity.enums.FaqType2;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FAQ extends Article {
    private String type2;
}
