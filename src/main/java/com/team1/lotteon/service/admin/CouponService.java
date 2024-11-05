/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : CouponService 생성
     수정이력
      - 2024/10/29 이도영 - 관리자 쿠폰 등록,출력
      - 2024/10/30 이도영 - 쿠폰 정보 개별 출력
*/

package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.Coupon;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.repository.Memberrepository.SellerMemberRepository;
import com.team1.lotteon.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final SellerMemberRepository sellerMemberRepository;
    private final ModelMapper modelMapper;
    // 버전 insert coupon
    public Coupon insertCoupon(Coupon couponDTO) {

        Coupon coupon = modelMapper.map(couponDTO,  Coupon.class);

        return couponRepository.save(coupon);
    }
    //발급된 쿠폰 출력 OR 검색 한 쿠폰 출력
    public NewPageResponseDTO<CouponDTO> selectCouponAll(NewPageRequestDTO newPageRequestDTO) {
        // Pageable 설정 시
        Pageable pageable = newPageRequestDTO.getPageable("couponid", false);

        Page<Coupon> couponPage;

        //검색 유형
        String type = newPageRequestDTO.getType();
        //검색 키워드
        String keyword = newPageRequestDTO.getKeyword();

        // 조건에 따른 필터링 적용
        if(type != null && keyword != null && !keyword.isEmpty()) {
            switch (type) {
                //쿠폰 번호로 검색
                case "cnumber":
                    try {
                        Long couponId = Long.parseLong(keyword); // 숫자인 경우에만 parse
                        couponPage = couponRepository.findByCouponid(couponId, pageable);
                    } catch (NumberFormatException e) {
                        // 숫자가 아닌 경우 전체 검색으로 처리하거나, 사용자에게 에러 메시지 반환 가능
                        couponPage = Page.empty(pageable); // 빈 페이지 반환
                    }
                    break;

                //쿠폰명으로 검색
                case "cname":
                    couponPage = couponRepository.findByCouponnameContaining(keyword,pageable);
                    break;
                //발급자 이름 으로 검색
                case "cprovider":
                    if ("Admin".equals(keyword) || "관리자".equals(keyword)) {
                        couponPage = couponRepository.findByMemberRole("Admin", pageable);
                    }  else {
                        // `shopName`으로 SellerMember 목록을 조회하고 UID를 추출하여 Long 리스트 생성
                        List<String> sellerMemberIds = sellerMemberRepository.findByShopShopNameContaining(keyword)
                                .stream()
                                .map(SellerMember::getUid) // Long 타입 UID로 변환
                                .collect(Collectors.toList());

                        // 필터링된 UID로 CouponRepository에서 쿠폰 조회
                        couponPage = couponRepository.findByMemberUidIn(sellerMemberIds, pageable);
                    }
                    break;
                default:
                    //조건이 없을 경우 전체 검색
                    couponPage = couponRepository.findAll(pageable);
                    break;
            }
        }else{
            //조건이 없을 경우 전체 검색
            couponPage = couponRepository.findAll(pageable);
        }
        // Coupon 엔티티를 CouponDTO로 변환하고 출력준비
        List<CouponDTO> couponDTOList = couponPage.getContent().stream().map(coupon -> {
            CouponDTO couponDTO = modelMapper.map(coupon, CouponDTO.class);
            // couponmakedate 분리 처리
            if (coupon.getCouponmakedate() != null) {
                String[] dateTimeParts = coupon.getCouponmakedate().toString().split("T");
                couponDTO.setCouponmakedate(dateTimeParts[0]); // 날짜 부분 설정

                // 시간 부분에서 소수점 이하를 제거하여 "HH:mm:ss" 형식으로 저장
                String timePart = dateTimeParts[1].split("\\.")[0];
                couponDTO.setCouponmaketime(timePart); // 시간 부분 설정 (초 이하 제외)
            }

            // 발급자 역할에 따라 issuerInfo 설정
            Member member = coupon.getMember();
            if ("Admin".equals(member.getRole())) {
                couponDTO.setIssuerInfo("관리자");
            } else if ("Seller".equals(member.getRole())) {
                Optional<SellerMember> sellerMemberOpt = sellerMemberRepository.findById(member.getUid());
                sellerMemberOpt.ifPresent(sellerMember -> {
                    String shopName = sellerMember.getShop() != null ? sellerMember.getShop().getShopName() : "미등록 상점";
                    couponDTO.setIssuerInfo(shopName);
                });
            }
            return couponDTO;
        }).collect(Collectors.toList());
        //결과를 NewPageResponseDTO에 담아 반환
        return NewPageResponseDTO.<CouponDTO>builder()
                .newPageRequestDTO(newPageRequestDTO) // 요청 정보 설정
                .dtoList(couponDTOList) // DTO 리스트 설정
                .total((int) couponPage.getTotalElements()) // 총 요소 수 설정
                .build();
    }
    //아이디 값을 활용해서 쿠폰 검색
    public Optional<CouponDTO> findCouponById(Long id) {
        return couponRepository.findById(id)
                .map(coupon -> {
                    CouponDTO couponDTO = modelMapper.map(coupon, CouponDTO.class);

                    // 발급자 역할에 따라 issuerInfo 설정
                    Member member = coupon.getMember();
                    if ("Admin".equals(member.getRole())) {
                        couponDTO.setIssuerInfo("관리자");
                    } else if ("Seller".equals(member.getRole())) {
                        // SellerMember를 안전하게 가져와 처리
                        Optional<SellerMember> sellerMemberOpt = sellerMemberRepository.findById(member.getUid());
                        sellerMemberOpt.ifPresent(sellerMember -> {
                            String shopName = sellerMember.getShop() != null ? sellerMember.getShop().getShopName() : "미등록 상점";
                            couponDTO.setIssuerInfo(shopName);
                        });
                    }

                    return couponDTO;
                });
    }
    
    //상점 아이디를 활용해서 쿠폰 정보 가지고 오기
    public List<Coupon> findCouponsByMemberId(String memberId) {
        log.info("dsfaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        // memberId로 쿠폰을 조회하여 전체 엔티티를 반환
        List<Coupon> DDD = couponRepository.findByMember_Uid(memberId);
        log.info("adfsdfsafda" + DDD.toString());

        return DDD;
    }
}
