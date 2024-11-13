package com.team1.lotteon.repository.review;

import com.team1.lotteon.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r join fetch r.product join fetch r.member WHERE r.product.id = :productId ORDER BY r.createdAt DESC")
    public Page<Review> findByProductId(@Param("productId") Long productId, Pageable pageable);

    public long countByProductId(Long productId);

    @Query("SELECT COALESCE(AVG(r.score), 0) FROM Review r WHERE r.product.id = :productId")
    public double findAverageScoreByProductId(@Param("productId") Long productId);

    @Query("SELECT r FROM Review r join fetch r.product WHERE r.member.uid = :uid ORDER BY r.createdAt DESC")
    public Page<Review> findWithProductByMemberUid(@Param("uid") String uid, Pageable pageable);

    public List<Review> findTop3ByMemberUidOrderByCreatedAtDesc(@Param("uid") String uid);
}
