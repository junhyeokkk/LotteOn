package com.team1.lotteon.entity.enums;

public enum DeliveryStatus {
    READY("배송준비중"),
    DELIVERED("배송중"),
    SHIPPED("배송완료");

    private final String koreanLabel;

    DeliveryStatus(String koreanLabel) {
        this.koreanLabel = koreanLabel;
    }

    public String getKoreanLabel() {
        return koreanLabel;
    }
}
