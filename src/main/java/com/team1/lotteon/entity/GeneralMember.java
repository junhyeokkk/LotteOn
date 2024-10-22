package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.Gender;
import com.team1.lotteon.entity.enums.Grade;
import com.team1.lotteon.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("General")
public class GeneralMember extends Member {
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.E;
    private int points;
    private String email;
    private String ph;
    private Status status;
    @Embedded
    private Address address;
    private LocalDateTime lastLoginDate;
    private String etc;

    public void setAddress(Address address) {
        this.address = address;
    }
}
