package com.nusiss.productservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
