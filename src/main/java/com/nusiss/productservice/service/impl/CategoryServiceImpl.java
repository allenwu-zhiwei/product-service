package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nusiss.commonservice.feign.UserFeignClient;
import com.nusiss.productservice.constant.MessageConstant;
import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.domain.entity.Category;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.exception.DeletionNotAllowedException;
import com.nusiss.productservice.mapper.CategoryMapper;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.result.PageApiResponse;
import com.nusiss.productservice.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.nusiss.commonservice.entity.User;
import com.nusiss.commonservice.config.ApiResponse;

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
    @Autowired
    private UserFeignClient userClient;

    /**
     * add category
     * @param categoryDTO
     */
    public void save(String authToken, CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);

        //time user
        category.setCreateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        category.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        // user info
        category.setCreateUser(queryCurrentUser(authToken));
        category.setUpdateUser(queryCurrentUser(authToken));

        categoryMapper.insert(category);
    }

    /**
     * query by page
     * @param categoryPageQueryDTO
     * @return
     */
    public PageApiResponse pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {

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

        return new PageApiResponse(page.getTotal(), page.getRecords());
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
    public void update(String authToken, CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //time user
        category.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        category.setUpdateUser(queryCurrentUser(authToken));

        categoryMapper.updateById(category);
    }

    /**
     * get user
     * @param authToken
     * @return
     */
    public String queryCurrentUser(String authToken) {
        ResponseEntity<ApiResponse<User>> res = userClient.getCurrentUserInfo(authToken);
        // Check the response status code
        if (res.getStatusCode() == HttpStatus.OK) {
            // Get the ApiResponse object
            ApiResponse<User> apiResponse = res.getBody();

            // Check if apiResponse is not null and extract the User object
            if (apiResponse != null) {
                User user = apiResponse.getData();
                return user.getUsername();
            }
            return "system";
        }
        return "system";
    }


}
