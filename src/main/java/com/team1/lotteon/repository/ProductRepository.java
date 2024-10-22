package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
