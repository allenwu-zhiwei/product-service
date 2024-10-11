package com.nusiss.productservice.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Category implements Serializable {
    @TableId
    private Long categoryId;

    private String categoryName;

    private String createUser;

    private String updateUser;

    private Timestamp createDatetime;

    private Timestamp updateDatetime;
}

