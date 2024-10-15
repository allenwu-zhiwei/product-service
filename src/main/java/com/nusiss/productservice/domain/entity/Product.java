package com.nusiss.productservice.domain.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id", nullable = false)
  private Long productId;

  @NotNull
  @Column(name = "seller_id", nullable = false)
  private Long sellerId;

  @Size(max = 255)
  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Lob
  @Column(name = "description")
  private String description;

  @NotNull
  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @NotNull
  @Column(name = "category_id", nullable = false)
  private Long categoryId;

  @Size(max = 100)
  @NotNull
  @Column(name = "create_user", nullable = false, length = 100)
  private String createUser;

  @Size(max = 100)
  @Column(name = "update_user", length = 100)
  private String updateUser;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "create_datetime")
  private Timestamp createDatetime;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "update_datetime")
  private Timestamp updateDatetime;

  // this field is for inventory
  // return this field for query method
  @TableField(exist = false)
  private int availableStock;

  // this field is for image
  @OneToMany
  @TableField(exist = false)
  private List<ProductImage> productImages;

}
