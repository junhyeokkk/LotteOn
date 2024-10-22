package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c left join fetch c.children WHERE c.parent IS NULL AND c.level='ONE'")
    public List<Category> findAllRootWithChildren();

    @Query("SELECT c FROM Category c left join fetch c.children left join fetch c.parent WHERE c.id = :id")
    public Category findWithChildrenAndParentById(@Param("id") Long id);
}
