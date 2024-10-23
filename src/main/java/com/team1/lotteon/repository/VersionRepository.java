package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
}
