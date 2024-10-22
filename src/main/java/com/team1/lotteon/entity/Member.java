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
@DiscriminatorColumn(name = "role")
public class Member extends BaseEntity {
    @Id
    private String uid;
    private String pass;

    public void setPass(String pass) {
        this.pass = pass;
    }
}
