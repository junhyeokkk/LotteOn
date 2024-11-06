package com.team1.lotteon.entity;

import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.entity.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 포인트 엔티티 생성
*/
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point")
public class Point{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;    // 지급내용

    private int givePoints;  // 지급 포인트
    private int acPoints;   // 잔여 포인트


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType; // 구분 (예: "적립", "사용", "만료")


    @CreationTimestamp
    private LocalDateTime createdat; // 지급날짜


    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private GeneralMember member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",  nullable = true)
    private Order order;


    private LocalDateTime expirationDate; // 유효기간 필드 추가


    public void changeMember(GeneralMember member){
        this.member = member;
    }

    public void setGivePoints(int givePoints) {
        this.givePoints = givePoints;
    }

    public void setAcPoints(int acPoints) {
        this.acPoints = acPoints;
    }



    // TransactionType 설정 메서드 추가
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }


    // 지급일로부터 12개월 후 유효기간 설정
    public void calculateExpirationDate() {
        if (this.createdat != null) {
            this.expirationDate = this.createdat.plusMonths(12);
            System.out.println("유효기간 설정됨: " + this.expirationDate); // 로그 추가
        } else {
            System.out.println("createdat이 null입니다. 유효기간을 설정할 수 없습니다.");
        }
    }
        public static PointDTO fromEntity(Point point) {
            GeneralMember member = point.getMember();
            return PointDTO.builder()
                    .member_id(member != null ? member.getUid() : null)
                    .type(point.getType())
                    .transactionType(point.getTransactionType()) // 추가된 부분
                    .givePoints(point.getGivePoints())
                    .acPoints(point.getAcPoints())
                    .createdat(point.getCreatedat())
                    .order_id(point.getOrder() != null ? point.getOrder().getId() : null) // 주문 번호가 있는 경우 설정
                    .build();
        }
    }




