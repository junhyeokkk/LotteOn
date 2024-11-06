package com.team1.lotteon.entity.enums;

public enum TransactionType {
    적립("적립"),   // 적립일 때 사용
    사용("사용"),   // 사용할 때 사용
    만료("만료");   // 만료일 때 사용

    private final String displayName;

    // 생성자 정의
    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    // displayName을 반환하는 메서드
    @Override
    public String toString() {
        return this.displayName;
    }
}


