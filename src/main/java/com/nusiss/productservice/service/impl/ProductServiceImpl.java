package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.result.PageResult;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * add product
     * @param productDTO
     */
    @Override
    public void save(ProductDTO productDTO) {
        Product product = new Product();
        product.setCreateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        product.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        BeanUtils.copyProperties(productDTO, product);

        productMapper.insert(product);
    }

    /**
     * page query for consumer
     * @param productPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQueryConsumer(ProductPageQueryDTO productPageQueryDTO) {

        IPage<Product> page = new Page<>(productPageQueryDTO.getPage(),productPageQueryDTO.getPageSize());

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        // set query condition
        // product name like
        if(!StringUtils.isEmpty(productPageQueryDTO.getName())){
            queryWrapper.like("name",productPageQueryDTO.getName());
        }
        // product description in
        if(!StringUtils.isEmpty(productPageQueryDTO.getDescription())){
            queryWrapper.in("description", productPageQueryDTO.getDescription());
        }
        // product category equal
        if(!StringUtils.isEmpty(productPageQueryDTO.getCategoryId())){
            queryWrapper.eq("category_id", productPageQueryDTO.getCategoryId());
        }

        productMapper.selectPage(page, queryWrapper);

        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * page query for merchant
     * @param productPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQueryMerchant(ProductPageQueryDTO productPageQueryDTO) {

        IPage<Product> page = new Page<>(productPageQueryDTO.getPage(),productPageQueryDTO.getPageSize());

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        // set query condition
        // product name like
        if(!StringUtils.isEmpty(productPageQueryDTO.getName())){
            queryWrapper.like("name",productPageQueryDTO.getName());
        }
        // product category equal
        if(!StringUtils.isEmpty(productPageQueryDTO.getCategoryId())){
            queryWrapper.eq("category_id", productPageQueryDTO.getCategoryId());
        }

        productMapper.selectPage(page, queryWrapper);

        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * modify product
     * @param productDTO
     */
    @Override
    public void update(ProductDTO productDTO) {
        Product product = new Product();
        product.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        BeanUtils.copyProperties(productDTO, product);

        productMapper.updateById(product);

    }

    /**
     * delete product
     * @param id
     */
    @Override
    public void deleteById(Long id) {

        productMapper.deleteById(id);
    }


}
