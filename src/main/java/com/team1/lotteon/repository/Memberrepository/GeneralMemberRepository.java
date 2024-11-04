package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.GeneralMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/*
  날짜 : 2024/10/25
  이름 : 이도영
  내용 : 일반회원 레포지토리

  수정사항
  - 2024/11/04 이도영  이메일 아이디 찾기 기능 추가
*/
@Repository
public interface GeneralMemberRepository extends JpaRepository<GeneralMember, String>{
    boolean existsByEmail(String uid);
    boolean existsByph(String ph);
    Optional<GeneralMember> findByUid(String uid);

    Page<GeneralMember> findByUidContaining(String uid, Pageable pageable);
    Page<GeneralMember> findByNameContaining(String name, Pageable pageable);
    Page<GeneralMember> findByEmailContaining(String email, Pageable pageable);
    Page<GeneralMember> findByPhContaining(String ph, Pageable pageable);
    List<GeneralMember> findByLastLoginDateBeforeAndStatus(LocalDateTime date, int status);
    GeneralMember findByNameAndEmail(String name, String email);
    List<GeneralMember> findAllByUidIn(List<String> uid);

    GeneralMember findByUidAndEmail(String uid, String email);
}
