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
public class Member extends BaseEntity {
    @Id
    private String uid;
    private String pass;
    private String role;

    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setRole(String role) {
        this.role = role;
    }


}



