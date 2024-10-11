package com.nusiss.productservice.domain.dto;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductDTO {

  private long productId;

  private long sellerId;

  private String name;

  private String description;

  private double price;

  private long categoryId;

}
