package com.nusiss.productservice.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Data
public class CategoryDTO implements Serializable {
    //@Schema(hidden = true)
    private Long categoryId;

    private String categoryName;
}

