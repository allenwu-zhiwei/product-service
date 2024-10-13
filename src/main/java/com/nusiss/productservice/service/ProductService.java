package com.nusiss.productservice.service;

import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.result.PageResult;

import java.util.List;

public interface ProductService {
    /**
     * add product
     * @param productDTO
     */
    void save(ProductDTO productDTO);

    /**
     * page query for consumer
     * @param productPageQueryDTO
     * @return
     */
    PageResult pageQueryConsumer(ProductPageQueryDTO productPageQueryDTO);

    /**
     * page query for merchant
     * @param productPageQueryDTO
     * @return
     */
    PageResult pageQueryMerchant(ProductPageQueryDTO productPageQueryDTO);


    /**
     * modify product
     * @param productDTO
     */
    void update(ProductDTO productDTO);

    /**
     * delete product
     * @param id
     */
    public void deleteById(Long id);
}
