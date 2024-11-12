package com.team1.lotteon.service;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.PageRequestDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
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

//    // 회원가입 축하 포인트
//    public void registerPoint(GeneralMemberDTO generalMemberDTO) {
//
//        PointDTO pointDTO = new PointDTO();
//        pointDTO.setType("회원가입 축하 포인트");
//        pointDTO.setGivePoints(1000);
//        // default 0이기 때문에 setGivePoints == setAcPoints
//        pointDTO.setAcPoints(1000);
//        pointDTO.setMember_id(generalMemberDTO.getUid());
//        log.info("포인트 서비스쪽 dto 멤버 " + generalMemberDTO);
//        insertPoint(pointDTO);
//    }


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

    // 유효기간이 지난 포인트를 소멸 상태로 업데이트
    public void expirePoints() {
        List<Point> points = pointRepository.findAll();

        points.forEach(point -> {
            if (point.getExpirationDate() != null && point.getExpirationDate().isBefore(LocalDateTime.now())) {
                point.setTransactionType(TransactionType.만료); // 포인트 상태를 "만료"로 설정
                pointRepository.save(point);
            }
        });
    }

    // 포인트 차감 메서드 추가
    public void deductPoints(PointDTO pointDTO) {
        int deductionPoints = Math.abs(pointDTO.getGivePoints()); // 차감 포인트 절대값으로 처리
        GeneralMember generalMember = generalMemberRepository.findByUid(pointDTO.getMember_id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (generalMember.getPoints() < deductionPoints) {
            throw new IllegalArgumentException("포인트가 부족하여 차감할 수 없습니다.");
        }

        generalMember.decreasePoints(deductionPoints);

        Point point = modelMapper.map(pointDTO, Point.class);
        point.setGivePoints(-deductionPoints); // 차감된 포인트는 음수로 저장
        pointDTO.setTransactionType(TransactionType.사용); // "사용"으로 설정
        point.setAcPoints(generalMember.getPoints());
        point.changeMember(generalMember);

        pointRepository.save(point);
    }


    // 포인트 합계 계산
    public int calculateTotalAcPoints(String uid) {
        List<Point> points = pointRepository.findByMemberUid(uid, Pageable.unpaged()).getContent();
        return points.stream().mapToInt(Point::getAcPoints).sum();
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
            GeneralMember generalMember = point.getMember();
            int deductionPoints = point.getGivePoints();

            log.info("Member ID: {}, 현재 포인트: {}, 차감 포인트: {}",
                    generalMember.getUid(), generalMember.getPoints(), deductionPoints);

            // 포인트 차감 및 업데이트
            if (generalMember.getPoints() >= deductionPoints) {
                generalMember.decreasePoints(deductionPoints);
                generalMemberRepository.saveAndFlush(generalMember); // DB에 즉시 반영

                log.info("Member ID: {}, 포인트 차감 후 포인트: {}", generalMember.getUid(), generalMember.getPoints());

                // 현재 상태를 기준으로 포인트 내역 재정렬 및 갱신
                int currentAcPoints = 0; // 초기화 후 재계산
                List<Point> remainingPoints = pointRepository.findByMemberOrderByCreatedatAsc(generalMember);

                // 중간 내역 삭제 후, 그 이후 내역의 acPoints를 재계산
                for (Point p : remainingPoints) {
                    if (!pointIds.contains(p.getId())) {
                        currentAcPoints += p.getGivePoints(); // 갱신된 포인트 값 적용
                        p.setAcPoints(currentAcPoints); // 갱신된 acPoints 설정
                        log.info("Point ID: {}, 갱신된 acPoints: {}", p.getId(), p.getAcPoints());
                        pointRepository.saveAndFlush(p); // 갱신된 내역 저장
                    }
                }

                // 포인트 내역 삭제
                pointRepository.delete(point);
                log.info("Point ID: {}, 포인트 내역이 삭제되었습니다.", point.getId());
            } else {
                log.warn("차감할 포인트가 현재 포인트보다 많습니다. Member ID: {}, 현재 포인트: {}, 차감 포인트: {}",
                        generalMember.getUid(), generalMember.getPoints(), deductionPoints);
            }
        }

        log.info("DB에 변경 사항이 반영되었습니다.");
    }

    @Transactional
    public void deductPoints(GeneralMember member, int discountPoint) {
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
        point.setGivePoints(0); // 지급 포인트는 0으로 설정
        point.setTransactionType(TransactionType.사용); // 트랜잭션 타입을 "사용"으로 설정
        point.setAcPoints(member.getPoints()); // 차감 후 잔여 포인트 설정
        point.changeMember(member);

        pointRepository.save(point); // 포인트 기록 저장

        // 로그 출력
        log.info("포인트 차감: 멤버 {} - 차감 포인트: {}, 잔여 포인트: {}", member.getUid(), discountPoint, member.getPoints());

    }











}