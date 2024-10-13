package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nusiss.productservice.constant.MessageConstant;
import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.domain.entity.Category;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.exception.DeletionNotAllowedException;
import com.nusiss.productservice.mapper.CategoryMapper;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.result.PageResult;
import com.nusiss.productservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * add category
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);

        //time user
        category.setCreateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        category.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        //category.setCreateUser(BaseContext.getCurrentId().toString());
        //category.setUpdateUser(BaseContext.getCurrentId().toString());

        categoryMapper.insert(category);
    }

    /**
     * query by page
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {

        IPage<Category> page = new Page<>(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        // set query condition
        if(!StringUtils.isEmpty(categoryPageQueryDTO.getCategoryId())){
            queryWrapper.eq("category_id",categoryPageQueryDTO.getCategoryId());
        }
        if(!StringUtils.isEmpty(categoryPageQueryDTO.getCategoryName())){
            queryWrapper.eq("category_name", categoryPageQueryDTO.getCategoryName());
        }

        categoryMapper.selectPage(page, queryWrapper);

        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * delete category
     * @param id
     */
    public void deleteById(Long id) {
        // Check if the current category is associated with any products. If it is associated, throw a business exception.
        QueryWrapper<Product> wrapper = new QueryWrapper();

        wrapper.eq("category_id", id);

        List<Product> product = productMapper.selectList(wrapper);

        if(!CollectionUtils.isEmpty(product)){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_PRODUCT);
        }

        categoryMapper.deleteById(id);
    }

    /**
     * update
     * @param categoryDTO
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //time user
        category.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        //category.setUpdateUser(BaseContext.getCurrentId().toString());

        categoryMapper.updateById(category);
    }

}