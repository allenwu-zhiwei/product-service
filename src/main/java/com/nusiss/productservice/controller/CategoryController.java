package com.nusiss.productservice.controller;

import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.result.PageResult;
import com.nusiss.productservice.result.Result;
import com.nusiss.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(name = "product category", description = "this API only for merchant")
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
    @Operation(summary = "add category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("add category：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * query by page
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "query by page. could query by categoryId or categoryName")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("query by page：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * delete category
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "delete category")
    public Result<String> deleteById(Long id){
        log.info("delete category：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * update
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "update")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

}
