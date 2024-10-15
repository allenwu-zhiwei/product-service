package com.nusiss.productservice.service;

import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.result.PageApiResponse;


public interface CategoryService {

    /**
     * add
     * @param categoryDTO
     */
    void save(String authToken, CategoryDTO categoryDTO);

    /**
     * query by page
     * @param categoryPageQueryDTO
     * @return
     */
    PageApiResponse pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * delete
     * @param id
     */
    void deleteById(Long id);

    /**
     * update
     * @param categoryDTO
     */
    void update(String authToken, CategoryDTO categoryDTO);


}
