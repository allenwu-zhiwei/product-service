package com.nusiss.productservice.domain.dto;
import lombok.Data;

@Data
public class ProductPageQueryDTO {
  private int page;

  private int pageSize;

  private Long productId;

  private Long sellerId;

  private String name;

  private String description;

  private double price;

  private Long categoryId;
  // this field is for inventory
  private int availableStock;

}
