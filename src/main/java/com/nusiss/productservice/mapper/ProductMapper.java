package com.nusiss.productservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusiss.productservice.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.StringUtils;


import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

/*
    @Select("select p.*, from nusmall_product.product p left join nusmall_product.product_Image i on p.product_id = i.product_id " +
            "where p.name like CONCAT('%', #{name}, '%') and p.category_id = #{categoryId} and p.create_user = #{createUser}" +
            " limit #{pageBegin},#{pageSize}")
    List<Product> findByPage(@Param("pageBegin") Integer PageBegin, @Param("PageSize")Integer PageSize);
*/

}
