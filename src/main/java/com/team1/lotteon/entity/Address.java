package com.team1.lotteon.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String zip;
    private String addr1;
    private String addr2;
}
