package com.nusiss.productservice.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private Long categoryId;

    private String categoryName;

}
