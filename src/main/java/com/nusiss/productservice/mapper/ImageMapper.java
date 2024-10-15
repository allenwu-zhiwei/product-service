package com.nusiss.productservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.domain.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;

import java.awt.*;

@Mapper
public interface ImageMapper extends BaseMapper<ProductImage> {

}
