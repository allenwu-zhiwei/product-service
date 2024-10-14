package com.nusiss.productservice.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Size(max = 100)
    @NotNull
    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

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
}

