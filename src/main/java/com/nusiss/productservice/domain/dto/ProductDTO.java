package com.nusiss.productservice.domain.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ProductDTO {
  //@Schema(hidden = true)
  private Long productId;

  private Long sellerId;

  private String name;

  private String description;

  private BigDecimal price;

  private Long categoryId;

  // this field is for inventory
  private int availableStock;

  // this field is for image;
  private List<String> imageUrls;

}
