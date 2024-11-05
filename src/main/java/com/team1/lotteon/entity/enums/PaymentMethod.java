package com.team1.lotteon.entity.enums;

public enum PaymentMethod {
    CREDIT_CARD("신용카드"),
    DEBIT_CARD("체크카드"),
    ACCOUNT_TRANSFER("무통장입금"),
    BANK_TRANSFER("계좌이체"),
    MOBILE_PAYMENT("휴대폰결제"),
    KAKAOPAY("카카오페이");

    private final String koreanLabel;

    PaymentMethod(String koreanLabel) {
        this.koreanLabel = koreanLabel;
    }

    public String getKoreanLabel() {
        return koreanLabel;
    }
}
