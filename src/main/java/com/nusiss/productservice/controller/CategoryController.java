package com.nusiss.productservice.controller;

import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.result.PageApiResponse;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(name = "product category", description = "These APIs only for merchant")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * add category
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "add category", description = "don't use categoryId, only use categoryName")
    public ApiResponse<String> save(@RequestHeader("authToken") String authToken, @RequestBody CategoryDTO categoryDTO){
        log.info("add category：{}", categoryDTO);
        categoryService.save(authToken, categoryDTO);
        return ApiResponse.success();
    }

    /**
     * query by page
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "query by page. could query by categoryId or categoryName")
    public ApiResponse<PageApiResponse> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("query by page：{}", categoryPageQueryDTO);
        PageApiResponse pageApiResponse = categoryService.pageQuery(categoryPageQueryDTO);
        return ApiResponse.success(pageApiResponse);
    }

    /**
     * delete category
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "delete category")
    public ApiResponse<String> deleteById(Long id){
        log.info("delete category：{}", id);
        categoryService.deleteById(id);
        return ApiResponse.success();
    }

    /**
     * update
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "update")
    public ApiResponse<String> update(@RequestHeader("authToken") String authToken, @RequestBody CategoryDTO categoryDTO){
        categoryService.update(authToken, categoryDTO);
        return ApiResponse.success();
    }

}
