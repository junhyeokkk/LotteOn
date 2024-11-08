package com.team1.lotteon.service.admin;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
     - 2024/11/03 이도영 선택된 쿠폰 저장 기능 구현
                        전체 쿠폰 저장 기능 구현
                        updateCouponUseStatusAndIncrementUse 기능 임시작업
     - 2024/11/05 이도영 발급된 쿠폰 정보 출력 모달
                        쿠폰 상태 업데이트 기능
                        판매자와 관리자에 따라 출력 방식 변경
     - 2024/11/06 이도영 나의정보에서 다운로드한 쿠폰 수 출력
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

    // 쿠폰 가져오기 (상점 정보를 이용해서)
    public List<CouponTake> findByMemberIdAndShopId(String memberId, List<Long> shopIds) {
        QCouponTake couponTake = QCouponTake.couponTake;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(couponTake.member.uid.eq(memberId))
                .and(couponTake.couponUseCheck.eq(0)); // couponusecheck가 0인 경우 추가

        // shopIds가 비어있지 않은 경우 shopIds 목록 내의 shopId만 조회
        if (shopIds != null && !shopIds.isEmpty()) {
            builder.and(couponTake.shop.id.in(shopIds));
        }

        return queryFactory.selectFrom(couponTake)
                .where(builder)
                .fetch();
    }

    // 상점 정보가 없어도 memberId가 같은 경우 가져오기
    public List<CouponTake> findByMemberIdAndNullShopId(String memberId) {
        QCouponTake couponTake = QCouponTake.couponTake;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(couponTake.member.uid.eq(memberId))
                .and(couponTake.shop.id.isNull()) // shopId가 null인 경우만 조회
                .and(couponTake.couponUseCheck.eq(0)); // couponusecheck가 0인 경우 추가

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
        // coupongive 값 증가 처리
        coupon.setCoupongive((coupon.getCoupongive() != null ? coupon.getCoupongive() : 0) + 1);
        couponRepository.save(coupon); // 변경된 coupongive 값으로 쿠폰 저장

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
            // coupongive 값 증가
            coupon.setCoupongive((coupon.getCoupongive() != null ? coupon.getCoupongive() : 0) + 1);
            couponRepository.save(coupon); // 변경된 coupongive 값 저장

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

    public NewPageResponseDTO<CouponTakeDTO> selectcoupontakeAll(NewPageRequestDTO newPageRequestDTO,String role, Long shopid) {
        Pageable pageable = newPageRequestDTO.getPageable("couponTakenId", false);
        Page<CouponTake> couponTakePage = null;

        String type = newPageRequestDTO.getType();
        String keyword = newPageRequestDTO.getKeyword();

        if (type != null && keyword != null && !keyword.isEmpty()) {  // 검색 조건이 있을 경우
            switch (type) {
                case "couponNo":
                    couponTakePage = "Seller".equals(role) && shopid != null
                            ? couponTakeRepository.findByCouponTakenIdAndShopId(Long.parseLong(keyword), shopid, pageable)
                            : couponTakeRepository.findByCouponTakenId(Long.parseLong(keyword), pageable);
                    break;
                case "couponId":
                    couponTakePage = "Seller".equals(role) && shopid != null
                            ? couponTakeRepository.findByCoupon_CouponidAndShopId(Long.parseLong(keyword), shopid, pageable)
                            : couponTakeRepository.findByCoupon_Couponid(Long.parseLong(keyword), pageable);
                    break;
                case "couponName":
                    couponTakePage = "Seller".equals(role) && shopid != null
                            ? couponTakeRepository.findByCoupon_CouponnameContainingAndShopId(keyword, shopid, pageable)
                            : couponTakeRepository.findByCoupon_CouponnameContaining(keyword, pageable);
                    break;
                default:
                    couponTakePage = "Seller".equals(role) && shopid != null
                            ? couponTakeRepository.findByShopId(shopid, pageable)
                            : couponTakeRepository.findAll(pageable);
                    break;
            }
        } else {  // 검색 조건이 없을 경우
            couponTakePage = "Seller".equals(role) && shopid != null
                    ? couponTakeRepository.findByShopId(shopid, pageable)
                    : couponTakeRepository.findAll(pageable);
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


    // GeneralMember와 coupon_id가 일치하는 CouponTake 엔티티를 조회 일치하면 사용 했음으로 변경 + 쿠폰에 사용횟수 증가
    public boolean updateCouponUseStatusAndIncrementUse(GeneralMember member, Long couponId) {
        // 1. CouponTake 엔티티에서 couponUseCheck 값을 2로 업데이트
        String memberId = member.getUid();
        Optional<CouponTake> optionalCouponTake = couponTakeRepository.findByMember_UidAndCoupon_Couponid(memberId, couponId);

        if (optionalCouponTake.isPresent()) {
            CouponTake couponTake = optionalCouponTake.get();
            couponTake.setCouponUseCheck(2); // couponusecheck 값을 2로 설정
            couponTakeRepository.save(couponTake); // 변경 사항 저장
        } else {
            return false; // 일치하는 CouponTake 항목을 찾지 못한 경우
        }

        // 2. Coupon 엔티티에서 couponuse 값을 +1 증가
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);

        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            coupon.setCouponuse(coupon.getCouponuse() + 1); // couponuse 값을 +1 증가
            couponRepository.save(coupon); // 변경 사항 저장
        } else {
            return false; // 일치하는 Coupon 항목을 찾지 못한 경우
        }
        return true; // 두 작업이 성공적으로 완료된 경우
    }

    //발급된 쿠폰 정보 출력 모달
    public Optional<CouponTakeDTO> findCouponTakeById(Long id) {
        return couponTakeRepository.findById(id)
                .map(couponTake -> {
                    CouponTakeDTO coupontakedto = modelMapper.map(couponTake, CouponTakeDTO.class);
                    log.info("쿠폰 아이디 :"+coupontakedto.getCouponId());
                    log.info("발급쿠폰 아이디 :"+coupontakedto.getCouponTakenId());
                    Member member = couponTake.getMember();
                    Coupon coupon = couponTake.getCoupon();
                    Shop shop = couponTake.getShop();
                    if (coupon != null) {
                        coupontakedto.setCouponId(coupon.getCouponid());//쿠폰번호
                        coupontakedto.setCouponName(coupon.getCouponname()); //쿠폰명
                        coupontakedto.setCouponDiscount(coupon.getCoupondiscount()); // 할인율(혜택)
                        coupontakedto.setCouponType(coupon.getCoupontype());    //쿠폰 종류
                        coupontakedto.setCouponetc(coupon.getCouponetc()); //상세정보
                    }
                    if(shop!=null) {
                        coupontakedto.setShopName(couponTake.getShop().getShopName());
                    }else{
                        coupontakedto.setShopName("롯데");
                    }
                    // 날짜 포맷 설정
                    coupontakedto.setFormattedDates();
                    return coupontakedto;
                });
    }

    public boolean updateCouponStatus(Long couponId, int couponUseCheck) {
        return couponTakeRepository.findById(couponId)
                .map(coupon -> {
                    coupon.setCouponUseCheck(couponUseCheck);
                    couponTakeRepository.save(coupon);
                    return true;
                })
                .orElse(false);
    }
    //2024/11/06 이도영 나의정보에서 다운로드한 쿠폰 수 출력
    //2024/11/08 이도영 나의정보에서 다운로드한 쿠폰중 사용하지 않는 내용만 출력
    public int getCouponCountByUserId(String memberId) {
        List<CouponTake> coupons = couponTakeRepository.findAllByMember_UidAndCouponUseCheck(memberId, 0);
        return coupons.size();
    }
    public void checkCouponTakes() {
        LocalDateTime startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endOfYesterday = LocalDate.now().minusDays(1).atTime(23, 59, 59, 999999999);

        // 어제 날짜 범위 내에 있고, couponusecheck가 0인 쿠폰만 조회
        List<CouponTake> coupons = couponTakeRepository.findByCouponExpireDateBetweenAndCouponUseCheck(
                startOfYesterday, endOfYesterday, 0);

        // 조회된 쿠폰들의 couponusecheck를 3으로 변경
        for (CouponTake coupon : coupons) {
            coupon.setCouponUseCheck(3);
        }

        // 변경된 쿠폰 정보를 DB에 저장
        couponTakeRepository.saveAll(coupons);
    }

    public List<CouponTakeDTO> findCouponsByMemberIdAndCouponUseCheck(String memberId) {
        // Repository에서 CouponTake 엔티티 리스트를 가져옴
        List<CouponTake> coupons = couponTakeRepository.findByMember_UidAndCouponUseCheck(memberId, 0);

        // CouponTake 엔티티 리스트를 CouponTakeDTO 리스트로 변환하여 반환
        return coupons.stream()
                .map(coupon -> new CouponTakeDTO(
                        coupon.getCoupon().getCouponname(),
                        coupon.getCoupon().getCoupondiscount(),
                        coupon.getShop() != null ? coupon.getShop().getShopName() : "모든 상점 이용 가능",
                        coupon.getCouponExpireDate()
                ))
                .collect(Collectors.toList());
    }
}
