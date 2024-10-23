package com.nusiss.productservice.service;

import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.result.PageApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    /**
     * add product
     * @param productDTO
     */
    void save(String authToken, ProductDTO productDTO);

    /**
     * page query for consumer
     * @param productPageQueryDTO
     * @return
     */
    PageApiResponse pageQueryConsumer(ProductPageQueryDTO productPageQueryDTO);

    /**
     * page query for merchant
     * @param productPageQueryDTO
     * @return
     */
    PageApiResponse pageQueryMerchant(String authToken, ProductPageQueryDTO productPageQueryDTO);


    /**
     * modify product
     * @param productDTO
     */
    void update(String authToken, ProductDTO productDTO);

    /**
     * delete product
     * @param id
     */
    public void deleteById(Long id);

    /**
     * query product
     * @param id
     */
    public Product queryById(Long id);

    /**
     * upload file
     * @param file
     * @return
     */
    public String upload(MultipartFile file);

    /**
     * delete file
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath);
}
