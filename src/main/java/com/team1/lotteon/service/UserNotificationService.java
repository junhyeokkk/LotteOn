package com.team1.lotteon.service;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService {

    public void notifyUserForCombinationRemoval(GeneralMember member, ProductOptionCombination combination) {
        String message = String.format(
                "장바구니에 담긴 '%s' 상품의 옵션 조합 '%s'이(가) 더 이상 유효하지 않습니다. 다른 옵션을 선택해 주세요.",
                combination.getProduct().getProductName(),
                combination.getOptionValueCombination()
        );

        // 이메일 전송 또는 알림 시스템을 통한 알림 예제
        sendNotification(member, message);  // 예: 이메일 전송, 알림 API 호출 등
    }

    private void sendNotification(GeneralMember member, String message) {
        // 실제 알림 전송 로직 (예: 이메일, SMS, 웹 알림 등)
        System.out.println("Notify " + member.getName() + ": " + message);
    }
}
