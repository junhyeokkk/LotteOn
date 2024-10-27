package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Config;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : config 리파지토리 생성
*/
@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {
}
