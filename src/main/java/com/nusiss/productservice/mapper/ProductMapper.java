package com.nusiss.productservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
