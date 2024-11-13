package com.team1.lotteon.service;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.PageRequestDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.entity.enums.TransactionType;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.PointRepository;
import com.team1.lotteon.service.Order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
*   날짜 : 2024/10/24
*   이름 : 최준혁
*   내용 : PointService 생성
*
* 수정이력
*   - 2025/11/03 박서홍 - 포인트 차감코드 추가
*   - 2025/11/04 박서홍 - 주문하기 - 포인트 사용 추가
*   - 2025/11/06 박서홍 - 마이페이지 - 포인트내역 추가
*
*/
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class PointService {
    private final GeneralMemberRepository generalMemberRepository;
    private final PointRepository pointRepository;
    private final ModelMapper modelMapper;




    // 포인트 지급 메서드
    public void registerPoint(GeneralMemberDTO generalMemberDTO, int points, String pointType) {
        PointDTO pointDTO = new PointDTO();
        pointDTO.setType(pointType);
        pointDTO.setTransactionType(TransactionType.적립); // "적립"으로 설정
        pointDTO.setGivePoints(points);
        pointDTO.setAcPoints(generalMemberDTO.getPoints() + points);
        pointDTO.setMember_id(generalMemberDTO.getUid());
        // 유효기간 설정: 현재 날짜로부터 12개월 후
        LocalDateTime expirationDate = LocalDateTime.now().plusMonths(12);
        pointDTO.setExpirationDate(expirationDate);

        log.info("포인트 지급: 멤버 " + generalMemberDTO + " - 포인트: " + points + ", 타입: " + pointType);



        // insertPoint에서 유효기간 설정과 엔티티 변환을 처리
        insertPoint(pointDTO);
    }

    // 포인트 insert
    public void insertPoint(PointDTO pointDTO) {
        // PointDTO를 Point 엔티티로 변환
        Point point = modelMapper.map(pointDTO, Point.class);

        // 유효기간 설정 (예: 12개월)
        point.calculateExpirationDate();

        // 회원 정보 조회 및 포인트 업데이트
        GeneralMember generalMember = generalMemberRepository.findByUid(pointDTO.getMember_id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        generalMember.increasePoints(pointDTO.getGivePoints());
        point.changeMember(generalMember);

        // 포인트 저장
        pointRepository.save(point);
    }




    @Transactional
    public void expirePoints() {
        List<Point> expiredPoints = pointRepository.findByExpirationDateBeforeAndTransactionType(LocalDateTime.now(), TransactionType.적립);

        expiredPoints.forEach(point -> {
            GeneralMember member = point.getMember();
            int expiredValue = point.getGivePoints();

            // 로그 추가 - 만료될 포인트 값 확인
            log.info("만료 처리 중 - 기존 givePoints 값: {}", expiredValue);

            // 회원의 잔여 포인트에서 만료된 포인트 차감
            member.decreasePoints(Math.min(member.getPoints(), expiredValue));
            generalMemberRepository.save(member);

            // 만료된 포인트 내역 생성 (음수 값으로 설정)
            Point expiredPoint = new Point();
            expiredPoint.setGivePoints(-Math.abs(expiredValue)); // 음수 값으로 설정
            expiredPoint.setTransactionType(TransactionType.만료);
            expiredPoint.setAcPoints(member.getPoints());
            expiredPoint.changeMember(member);
            expiredPoint.setExpirationDate(point.getExpirationDate());

            // 로그 추가 - 생성된 만료 포인트 값 확인
            log.info("생성된 만료 포인트 - givePoints 값: {}", expiredPoint.getGivePoints());

            pointRepository.save(expiredPoint);
        });

        log.info("모든 만료 포인트 처리가 완료되었습니다.");
    }
    @Transactional
    public void deductPoints(GeneralMember member, int discountPoint, Order order) {
        // 1. 차감할 포인트 값 (절대값으로 처리)
        discountPoint = Math.abs(discountPoint);

        // 2. 잔여 포인트 확인
        if (member.getPoints() < discountPoint) {
            throw new IllegalArgumentException("포인트가 부족하여 차감할 수 없습니다.");
        }

        // 3. GeneralMember의 포인트 차감
        member.decreasePoints(discountPoint);
        generalMemberRepository.save(member); // 변경 사항 저장

        // 4. 포인트 기록 생성 (차감된 포인트 기록)
        Point point = new Point();
        point.setGivePoints(-discountPoint); // 지급 포인트는 0으로 설정
        point.setTransactionType(TransactionType.사용); // 트랜잭션 타입을 "사용"으로 설정
        point.setAcPoints(member.getPoints()); // 차감 후 잔여 포인트 설정
        point.changeMember(member);
        point.setOrder(order); // 주문 정보 설정
        point.setType("상품구매시 사용");

        pointRepository.save(point); // 포인트 기록 저장

        // 로그 출력
        log.info("포인트 차감: 멤버 {} - 차감 포인트: {}, 잔여 포인트: {}", member.getUid(), discountPoint, member.getPoints());

    }



    public int calculateTotalAcPoints(String uid) {
        // 모든 포인트 내역 조회
        List<Point> points = pointRepository.findByMemberUid(uid, Pageable.unpaged()).getContent();

        // 적립된 포인트 합계
        int totalEarned = points.stream()
                .filter(point -> point.getTransactionType() == TransactionType.적립)
                .mapToInt(Point::getGivePoints)
                .sum();

        // 사용된 포인트 합계 (음수 값)
        int totalUsed = points.stream()
                .filter(point -> point.getTransactionType() == TransactionType.사용)
                .mapToInt(Point::getGivePoints)
                .sum();

        // 만료된 포인트 합계 (음수 값)
        int totalExpired = points.stream()
                .filter(point -> point.getTransactionType() == TransactionType.만료)
                .mapToInt(Point::getGivePoints)
                .sum();

        // 실제 잔여 포인트 계산 (적립 포인트 - 사용된 포인트 - 만료된 포인트)
        int remainingPoints = totalEarned + totalUsed + totalExpired;

        log.info("총 적립 포인트: {}, 총 사용 포인트: {}, 총 만료 포인트: {}, 잔여 포인트: {}",
                totalEarned, totalUsed, totalExpired, remainingPoints);

        return remainingPoints;
    }


    // 포인트 select 페이징 (MYPAGE) => 내 포인트 가져오기
    public PointPageResponseDTO getMyPoints(PointPageRequestDTO pointPageRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = (authentication != null && authentication.getPrincipal() instanceof UserDetails)
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : null;

        log.info("현재 로그인한 사용자 uid: {}", uid);
        log.info("Service 검색 조건 - startDate: {}, endDate: {}", pointPageRequestDTO.getStartDate(), pointPageRequestDTO.getEndDate());

        Pageable pageable = pointPageRequestDTO.getPageable("createdat");
        Page<Point> pointPage;

        // 날짜 조건에 따른 조회
        if (pointPageRequestDTO.getStartDate() != null && !pointPageRequestDTO.getStartDate().isEmpty()) {
            LocalDateTime startDateTime = LocalDate.parse(pointPageRequestDTO.getStartDate()).atStartOfDay();
            LocalDateTime endDateTime = (pointPageRequestDTO.getEndDate() != null && !pointPageRequestDTO.getEndDate().isEmpty())
                    ? LocalDate.parse(pointPageRequestDTO.getEndDate()).atTime(LocalTime.MAX)
                    : LocalDateTime.now();


            pointPage = pointRepository.findByMember_UidAndCreatedatBetween(uid, startDateTime, endDateTime, pageable);
        } else {
            pointPage = pointRepository.findByMemberUid(uid, pageable);
        }


        // Point 엔티티를 DTO로 변환
        List<PointDTO> dtoList = pointPage.getContent().stream()
                .map(point -> modelMapper.map(point, PointDTO.class))
                .collect(Collectors.toList());


        // PointPageResponseDTO 생성 및 반환
        return new PointPageResponseDTO(pointPageRequestDTO, dtoList, (int) pointPage.getTotalElements());
    }




    // 포인트 select 페이징 (ADMIN) + 검색기능
    public PointPageResponseDTO getPoints(PointPageRequestDTO pointPageRequestDTO) {
        // Pageable 생성
        Pageable pageable = pointPageRequestDTO.getPageable("createdat");

        // 포인트 데이터 가져오기 (type과 keyword로 필터링)
        Page<Point> pointPage;

        // 타입이 "all"인 경우 모든 데이터 가져오기
        if ("all".equals(pointPageRequestDTO.getType())) {
            pointPage = pointRepository.findAll(pageable);
        } else if (pointPageRequestDTO.getType() != null && !pointPageRequestDTO.getType().isEmpty() &&
                (pointPageRequestDTO.getKeyword() != null && !pointPageRequestDTO.getKeyword().isEmpty() ||
                        pointPageRequestDTO.getKeyword() == null || pointPageRequestDTO.getKeyword().isEmpty())) {

            // QueryDSL을 사용하여 동적 쿼리 실행
            pointPage = pointRepository.findByDynamicType(pointPageRequestDTO.getKeyword(), pointPageRequestDTO.getType(), pageable);
        } else {
            // 조건이 맞지 않을 경우 모든 데이터를 가져옴
            pointPage = pointRepository.findAll(pageable);
        }

        // Point 엔티티를 DTO로 변환
        List<PointDTO> dtoList = pointPage.getContent().stream()
                .map(point -> modelMapper.map(point, PointDTO.class))
                .collect(Collectors.toList());

        // PointPageResponseDTO 생성 및 반환
        return new PointPageResponseDTO(pointPageRequestDTO, dtoList, (int) pointPage.getTotalElements());
    }

    @Transactional
    public void deleteSelectedPoints(List<Long> pointIds) {
        log.info("deleteSelectedPoints 서비스 메서드가 호출되었습니다. pointIds: {}", pointIds);

        List<Point> pointsToDelete = pointRepository.findAllById(pointIds);
        log.info("조회된 포인트 내역 수: {}", pointsToDelete.size());

        if (pointsToDelete.isEmpty()) {
            log.info("삭제할 포인트 내역이 없습니다.");
            return;
        }

        for (Point point : pointsToDelete) {
            GeneralMember member = point.getMember();
            int pointValue = point.getGivePoints();

            log.info("Member ID: {}, 현재 포인트: {}, 삭제할 포인트: {}, 트랜잭션 타입: {}",
                    member.getUid(), member.getPoints(), pointValue, point.getTransactionType());

            // 적립된 포인트 삭제 시: 잔여 포인트에서 차감
            if (point.getTransactionType() == TransactionType.적립) {
                member.decreasePoints(Math.min(member.getPoints(), pointValue));
                log.info("적립된 포인트 삭제 - 차감 후 잔여 포인트: {}", member.getPoints());
            }
            // 사용된 포인트 삭제 시: 잔여 포인트 복구
            else if (point.getTransactionType() == TransactionType.사용) {
                member.increasePoints(pointValue);
                log.info("사용된 포인트 삭제 - 복구 후 잔여 포인트: {}", member.getPoints());
            }

            // 회원 정보 업데이트
            generalMemberRepository.saveAndFlush(member);

            // 포인트 내역 삭제
            pointRepository.delete(point);
            log.info("Point ID: {}, 포인트 내역이 삭제되었습니다.", point.getId());
        }

        log.info("DB에 변경 사항이 반영되었습니다.");
    }









}