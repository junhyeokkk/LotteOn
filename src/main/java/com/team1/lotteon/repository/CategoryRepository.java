package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리 리파지토리 생성
*/
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c  WHERE c.parent IS NULL AND c.level=1 ORDER BY c.displayOrder ASC")
    public List<Category> findAllRoot();

    @Query("SELECT c FROM Category c  left join fetch c.parent WHERE c.id = :id")
    public Optional<Category>  findWithChildrenAndParentById(@Param("id") Long id);

    @Query("SELECT c FROM Category c left join fetch c.parent WHERE c.id = :id")
    public Optional<Category> findWithParentById(@Param("id") Long id);

    @Query("SELECT c FROM Category c  WHERE c.id = :id")
    public Optional<Category>  findWithChildrenById(@Param("id") Long id);

    @Query("SELECT COALESCE(MAX(c.displayOrder), 0) FROM Category c WHERE c.level=1 AND c.parent IS NULL")
    Integer findMaxDisplayOrderByRoot();

    @Query("SELECT COALESCE(MAX(c.displayOrder), 0) FROM Category c WHERE c.parent.id = :parentId")
    Integer findMaxDisplayOrderByParentId(@Param("parentId") Long parentId);

    public Optional<Category> findByIdAndLevel(Long id, int level);

    // 자식 카테고리 가져오기(준혁)
    List<Category> findByParentIdOrderByDisplayOrderAsc(Long parentId);

    public List<Category> findByLevelOrderByDisplayOrderAsc(int level);
}
