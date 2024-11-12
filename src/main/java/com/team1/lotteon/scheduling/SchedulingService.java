package com.team1.lotteon.scheduling;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.BannerService;
import com.team1.lotteon.service.admin.CouponService;
import com.team1.lotteon.service.admin.CouponTakeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
    날짜 : 2024/11/01
    이름 : 박서홍
    내용 : Scheduling 을 위한 Service 작성

*/
@Log4j2
@Service
@RequiredArgsConstructor
public class SchedulingService {


    private final GeneralMemberRepository generalMemberRepository;
    public static final int STATUS_ACTIVE = 1;     // 정상 상태
    public static final int STATUS_INACTIVE = 3;     // 휴면 상태
    private final PointService pointService;
    private final CouponService couponService;
    private final CouponTakeService couponTakeService;
    private final BannerService bannerService;


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
//@Scheduled(cron = "0 0/1 * * * ?") // 매 1분마다 실행 test용

    public void updateDormantMembers() {
        this.checkAndSetDormantMembers();
    }


    public void checkAndSetDormantMembers() {
//        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1); // 하루 전 시각 test용

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
//         '정상' 상태인 회원들 중에서 마지막 로그인 날짜가 3개월 전인 회원들을 가져옴
        List<GeneralMember> dormantMembers = generalMemberRepository.findByLastLoginDateBeforeAndStatus(threeMonthsAgo, STATUS_ACTIVE);
//        List<GeneralMember> dormantMembers = generalMemberRepository.findByLastLoginDateBeforeAndStatus(oneDayAgo, STATUS_ACTIVE); //test용

        for (GeneralMember member : dormantMembers) {
            member.setStatus(STATUS_INACTIVE); // '휴면' 상태로 변경
            try {
                generalMemberRepository.save(member);
                System.out.println("회원 " + member.getUid() + "의 상태가 휴면으로 전환되었습니다.");
            } catch (Exception e) {
                System.err.println("회원 " + member.getUid() + "의 상태 업데이트 중 오류 발생: " + e.getMessage());
            }
        }

    }

    // 포인트 만료 처리 (매일 자정 실행)
    @Scheduled(cron = "1 0 0 * * ?")
    public void expirePoints() {
        pointService.expirePoints(); // 포인트 만료 처리 메서드 호출
        System.out.println("만료된 포인트가 처리되었습니다.");
    }
    //쿠폰 기간 처리 (도영)
    @Scheduled(cron = "1 0 0 * * ?")
    public void Coupons() {
        couponService.checkCoupons();
    }
    //발급받은 쿠폰 기간 처리 (도영)
    @Scheduled(cron = "1 0 0 * * ?")
    public void CouponTakes() {
        couponTakeService.checkCouponTakes();
    }
    //배너 상태 변경(도영) 2024/11/12
//    @Scheduled(cron = "0 0 * * * *")
//    public void bannerSetting() {
//        bannerService.changeBannerStatues();
//        log.info("Start bannerSetting");
//    }

}
