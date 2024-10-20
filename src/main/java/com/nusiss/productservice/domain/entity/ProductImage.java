package com.nusiss.productservice.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@TableName("product_Image")
public class ProductImage {
    @Id
    @TableId(type= IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Size(max = 255)
    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Size(max = 100)
    @NotNull
    @Column(name = "create_user", nullable = false, length = 100)
    private String createUser;

    @Size(max = 100)
    @Column(name = "update_user", length = 100)
    private String updateUser;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_datetime")
    private Instant createDatetime;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_datetime")
    private Instant updateDatetime;

}