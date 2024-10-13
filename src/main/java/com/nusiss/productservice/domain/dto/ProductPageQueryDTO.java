package com.nusiss.productservice.domain.dto;
import lombok.Data;

@Data
public class ProductPageQueryDTO {
  private int page;

  private int pageSize;

  private long productId;

  private long sellerId;

  private String name;

  private String description;

  private double price;

  private long categoryId;

}
