package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Member extends BaseEntity {
    @Id
    private String uid;
    private String pass;
}
