package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.enums.CateLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c left join fetch c.children WHERE c.parent IS NULL AND c.level='ONE'")
    public List<Category> findAllRootWithChildren();

    @Query("SELECT c FROM Category c left join fetch c.children left join fetch c.parent WHERE c.id = :id")
    public Optional<Category>  findWithChildrenAndParentById(@Param("id") Long id);

    @Query("SELECT c FROM Category c left join fetch c.children WHERE c.id = :id")
    public Optional<Category>  findWithChildrenById(@Param("id") Long id);

    public Optional<Category> findByIdAndLevel(Long id, CateLevel level);
}
