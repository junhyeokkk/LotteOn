package com.team1.lotteon.apiController.product;


import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductCreateDTO;
import com.team1.lotteon.dto.product.ProductResponseDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        ProductResponseDTO product = productService.createProduct(productCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<ProductSummaryResponseDTO>> getProducts(@PageableDefault Pageable pageable) {
        System.out.println("pageable = " + pageable);
        PageResponseDTO<ProductSummaryResponseDTO> products = productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
