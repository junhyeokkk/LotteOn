package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {
    private String name;
    private String description;
    private String manufacturer;
    private int price;
    private int discountRate;
    private int point;
    private int stock;
    private int deliveryFee;
    private Long categoryId;

    public Product toEntity() {
        return Product.builder()
                .productName(name)
                .description(description)
                .manufacturer(manufacturer)
                .price(price)
                .discountRate(discountRate)
                .point(point)
                .stock(stock)
                .deliveryFee(deliveryFee)
                .build();
    }
}
