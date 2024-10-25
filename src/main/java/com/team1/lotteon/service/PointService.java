package com.team1.lotteon.service;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.PageRequestDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class PointService {
    private final GeneralMemberRepository generalMemberRepository;
    private final PointRepository pointRepository;
    private final ModelMapper modelMapper;

    // 회원가입 축하 포인트
    public void registerPoint(GeneralMemberDTO generalMemberDTO) {

        PointDTO pointDTO = new PointDTO();
        pointDTO.setType("회원가입 축하 포인트");
        pointDTO.setGivePoints(1000);
        // default 0이기 때문에 setGivePoints == setAcPoints
        pointDTO.setAcPoints(1000);
        pointDTO.setMember_id(generalMemberDTO.getUid());
        log.info("포인트 서비스쪽 dto 멤버 " + generalMemberDTO);
        insertPoint(pointDTO);
    }

    // 포인트 insert
    public void insertPoint(PointDTO pointDTO) {

        Point point = modelMapper.map(pointDTO, Point.class);
        GeneralMember generalMember = generalMemberRepository.findByUid(pointDTO.getMember_id()).orElse(null);
        // 멤버 포인트 set
        generalMember.increasePoints(pointDTO.getAcPoints());
        point.changeMember(generalMember);
        pointRepository.save(point);
    }

    // 포인트 select  페이징
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




}
