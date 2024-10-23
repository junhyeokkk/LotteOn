package com.team1.lotteon.apiController.product;


import com.team1.lotteon.dto.product.CreateProductDTO;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        productService.createProduct(createProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
