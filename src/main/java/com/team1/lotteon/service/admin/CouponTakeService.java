package com.team1.lotteon.service.admin;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.coupon.CouponRepository;
import com.team1.lotteon.repository.coupontake.CouponTakeRepository;
import com.team1.lotteon.repository.coupontake.CouponTakeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
     날짜 : 2024/10/30
     이름 : 이도영(최초 작성자)
     내용 : CouponTakeService 생성

     수정내용
     - 2024/11/01 이도영 나의정보에서 다운로드한 쿠폰 출력
                        관리자 쿠폰발급현황 기능 구현
*/
@Log4j2
@RequiredArgsConstructor
@Service
public class CouponTakeService {
    private final CouponTakeRepositoryCustom couponTakeRepositoryCustom;
    private final ModelMapper modelMapper;
    private final CouponTakeRepository couponTakeRepository;
    private final CouponRepository couponRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final JPAQueryFactory queryFactory;
    //쿠폰 가지고 오기(상점 정보를 이용해서)
    public List<CouponTake> findByMemberIdAndOptionalShopId(String memberId, Long shopId) {
        BooleanBuilder builder = new BooleanBuilder();
        QCouponTake couponTake = QCouponTake.couponTake;

        builder.and(couponTake.member.uid.eq(memberId))
                .and(shopId != null ? couponTake.shop.id.eq(shopId).or(couponTake.shop.id.isNull()) : couponTake.shop.id.isNull());

        return queryFactory.selectFrom(couponTake)
                .where(builder)
                .fetch();
    }

    //나의 정보에서 쿠폰 가지고 오기(멤버 정보만 활용해서)
    public Page<CouponTakeDTO> findPagedCouponsByMemberId(String memberId, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return couponTakeRepositoryCustom.findPagedCouponsByMemberId(memberId, pageable)
                .map(couponTake -> CouponTakeDTO.builder()
                        .couponTakenId(couponTake.getCouponTakenId())
                        .couponId(couponTake.getCoupon().getCouponid())
                        .memberId(couponTake.getMember().getUid())
                        .shopId(couponTake.getShop() != null ? couponTake.getShop().getId() : null)
                        .shopName(couponTake.getShop() != null ? couponTake.getShop().getShopName() : "모든 상점 사용 가능") // 상점 이름 기본값 설정
                        .couponGetDate(couponTake.getCouponGetDate())
                        .couponExpireDate(couponTake.getCouponExpireDate())
                        .couponExpireDateFormatted(
                                couponTake.getCouponExpireDate() != null
                                        ? couponTake.getCouponExpireDate().format(formatter)
                                        : null) // 포맷된 날짜 설정
                        .couponUseDate(couponTake.getCouponUseDate())
                        .couponUseCheck(couponTake.getCouponUseCheck())
                        .couponName(couponTake.getCoupon().getCouponname())
                        .couponDiscount(couponTake.getCoupon().getCoupondiscount())
                        .build()
                );
    }

    //선택한 쿠폰 저장 하기
    public CouponTakeDTO saveCouponTake(String memberid, Long shopid, Long couponid) {
        Coupon coupon = couponRepository.findById(couponid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        boolean exists = couponTakeRepository.existsByMember_UidAndCoupon_Couponid(memberid, couponid);
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 저장한 쿠폰 입니다");
        }
        LocalDateTime couponGetDate = LocalDateTime.now();
        LocalDateTime couponExpireDate = couponGetDate.plusDays(coupon.getCouponperiod());
        CouponTake couponTake = CouponTake.builder()
                .coupon(coupon)
                .member(Member.builder().uid(memberid).build())
                .shop(shopid != null ? Shop.builder().id(shopid).build() : null)
                .couponGetDate(couponGetDate)
                .couponExpireDate(couponExpireDate)
                .build();

        CouponTake savedCouponTake = couponTakeRepository.save(couponTake);

        return modelMapper.map(savedCouponTake, CouponTakeDTO.class);
    }

    //쿠폰 전체 저장 하기
    public List<CouponTakeDTO> saveCouponTakeList(String memberid, Long shopid, List<Long> couponIds) {
        List<CouponTakeDTO> savedCouponTakes = new ArrayList<>();
        for (Long couponid : couponIds) {
            Coupon coupon = couponRepository.findById(couponid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

            // 중복 확인
            boolean exists = couponTakeRepository.existsByMember_UidAndCoupon_Couponid(memberid, couponid);
            if (exists) {
                continue; // 이미 저장된 쿠폰이면 넘어감
            }

            LocalDateTime couponGetDate = LocalDateTime.now();
            LocalDateTime couponExpireDate = couponGetDate.plusDays(coupon.getCouponperiod());
            CouponTake couponTake = CouponTake.builder()
                    .coupon(coupon)
                    .member(Member.builder().uid(memberid).build())
                    .shop(shopid != null ? Shop.builder().id(shopid).build() : null)
                    .couponGetDate(couponGetDate)
                    .couponExpireDate(couponExpireDate)
                    .build();

            CouponTake savedCouponTake = couponTakeRepository.save(couponTake);
            savedCouponTakes.add(modelMapper.map(savedCouponTake, CouponTakeDTO.class));
        }

        return savedCouponTakes;
    }
    public NewPageResponseDTO<CouponTakeDTO> selectcoupontakeAll(NewPageRequestDTO newPageRequestDTO) {
        Pageable pageable = newPageRequestDTO.getPageable("couponTakenId", false);
        Page<CouponTake> couponTakePage = null;

        String type = newPageRequestDTO.getType();
        String keyword = newPageRequestDTO.getKeyword();

        if (type != null && keyword != null && !keyword.isEmpty()) {  // null 체크 추가
            switch (type) {
                case "couponNo":
                    couponTakePage = couponTakeRepository.findByCouponTakenId(Long.parseLong(keyword), pageable);
                    break;
                case "couponId":
                    couponTakePage = couponTakeRepository.findByCoupon_Couponid(Long.parseLong(keyword), pageable);
                    break;
                case "couponName":
                    couponTakePage = couponTakeRepository.findByCoupon_CouponnameContaining(keyword, pageable);
                    break;
                default:
                    couponTakePage = couponTakeRepository.findAll(pageable);
                    break;
            }
        } else {
            couponTakePage = couponTakeRepository.findAll(pageable);
        }

        AtomicInteger startNumber = new AtomicInteger((newPageRequestDTO.getPg() - 1) * newPageRequestDTO.getSize() + 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<CouponTakeDTO> couponTakeDTOList = couponTakePage.getContent().stream()
                .map(couponTake -> {
                    CouponTakeDTO couponTakeDTO = modelMapper.map(couponTake, CouponTakeDTO.class);
                    couponTakeDTO.setCouponId(couponTake.getCoupon().getCouponid());
                    couponTakeDTO.setCouponType(couponTake.getCoupon().getCoupontype());
                    couponTakeDTO.setCouponGetDate(couponTake.getCouponGetDate());
                    couponTakeDTO.setCouponExpireDate(couponTake.getCouponExpireDate());
                    couponTakeDTO.setCouponUseDate(couponTake.getCouponUseDate());
                    couponTakeDTO.setCouponUseCheck(couponTake.getCouponUseCheck());
                    couponTakeDTO.setCouponName(couponTake.getCoupon().getCouponname());
                    couponTakeDTO.setCouponUseDateFormatted(
                            couponTake.getCouponUseDate() != null
                                    ? couponTake.getCouponUseDate().format(formatter)
                                    : null
                    );
                    String generalMemberName = generalMemberRepository.findById(couponTake.getMember().getUid())
                            .map(GeneralMember::getName)
                            .orElse(null);
                    couponTakeDTO.setUsername(generalMemberName);
                    return couponTakeDTO;
                }).collect(Collectors.toList());

        return NewPageResponseDTO.<CouponTakeDTO>builder()
                .newPageRequestDTO(newPageRequestDTO)
                .dtoList(couponTakeDTOList)
                .total((int) couponTakePage.getTotalElements())
                .build();
    }

    // member_id와 coupon_id가 일치하는 CouponTake 엔티티를 조회 일치하면 사용 했음으로 변경
    @Transactional
    public boolean updateCouponUseCheck(String memberId, Long couponId) {

        Optional<CouponTake> optionalCouponTake = couponTakeRepository.findByMember_UidAndCoupon_Couponid(memberId, couponId);

        if (optionalCouponTake.isPresent()) {
            CouponTake couponTake = optionalCouponTake.get();
            couponTake.setCouponUseCheck(2); // couponusecheck 값을 2로 설정
            couponTakeRepository.save(couponTake); // 변경 사항 저장
            return true; // 업데이트 성공
        } else {
            return false; // 일치하는 항목을 찾지 못한 경우
        }
    }
}
