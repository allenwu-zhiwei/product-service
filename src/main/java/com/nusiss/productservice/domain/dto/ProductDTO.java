package com.nusiss.productservice.domain.dto;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ProductDTO {

  private Long productId;

  private Long sellerId;

  private String name;

  private String description;

  private double price;

  private Long categoryId;

  // this field is for inventory
  private int availableStock;

  // this field is for image;
  private List<String> imageUrls;

}
