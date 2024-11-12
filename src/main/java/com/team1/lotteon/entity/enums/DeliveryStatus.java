package com.team1.lotteon.entity.enums;

public enum DeliveryStatus {
    READY("배송준비중"),
    DELIVERED("배송중"),
    COMPLETE("수취완료"),
    REFUNDREQ("반품신청"),
    RETURNREQ("교환신청"),
    SHIPPED("배송완료");

    private final String koreanLabel;

    DeliveryStatus(String koreanLabel) {
        this.koreanLabel = koreanLabel;
    }

    public String getKoreanLabel() {
        return koreanLabel;
    }
}
