package com.nusiss.productservice.service;

import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.result.PageResult;


public interface CategoryService {

    /**
     * add
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * query by page
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * delete
     * @param id
     */
    void deleteById(Long id);

    /**
     * update
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);


}
