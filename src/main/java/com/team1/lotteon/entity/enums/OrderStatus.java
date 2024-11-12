package com.team1.lotteon.entity.enums;

public enum OrderStatus {
    ORDERED("주문완료"),
    DELIVERING("배송중"),
    DELIVERED("배송완료"),
    COMPLETE("수취완료"),
    CANCELLED("주문취소");

    private final String koreanLabel;

    OrderStatus(String koreanLabel) {
        this.koreanLabel = koreanLabel;
    }

    public String getKoreanLabel() {
        return koreanLabel;
    }
}
