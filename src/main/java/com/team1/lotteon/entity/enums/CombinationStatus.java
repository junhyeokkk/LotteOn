package com.team1.lotteon.entity.enums;

public enum CombinationStatus {
    SALE("판매중"),
    SOLDOUT("품절"),
    STOP("판매중지");

    private final String koreanLabel;

    CombinationStatus(String koreanLabel) {
        this.koreanLabel = koreanLabel;
    }

    public String getKoreanLabel() {
        return koreanLabel;
    }

    // String 값에서 CombinationStatus enum으로 변환하는 메서드
    public static CombinationStatus fromString(String status) {
        try {
            return CombinationStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid CombinationStatus value: " + status);
        }
    }
}
