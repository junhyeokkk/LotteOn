package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c join fetch c.children")
    public List<Category> findAllWithCategory();
}
