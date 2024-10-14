package com.nusiss.productservice.controller;

import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.result.PageResult;
import com.nusiss.productservice.result.Result;
import com.nusiss.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "product info" , description = "These APIs for merchant and consumer, consumer only use pageConsumer method.")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * add product
     * @param productDTO
     * @return
     */
    @PostMapping()
    @Operation(summary = "add product")
    public Result save(@RequestBody ProductDTO productDTO) {
        log.info("add product：{}", productDTO);
        productService.save(productDTO);

        return Result.success();
    }

    /**
     * page query for consumer
     * @param productPageQueryDTO
     * @return
     */
    @GetMapping("/page/consumer")
    @Operation(summary = "page query for consumer", description ="support query by product name(%), description(in), categoryId(=)")
    public Result<PageResult> pageConsumer(ProductPageQueryDTO productPageQueryDTO) {
        log.info("page query for consumer:{}", productPageQueryDTO);
        PageResult pageResult = productService.pageQueryConsumer(productPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * page query merchant
     * @param productPageQueryDTO
     * @return
     */
    @GetMapping("/page/merchant")
    @Operation(summary = "page query for merchant", description ="support query by product name(%), categoryId(=), only query products that they create ")
    public Result<PageResult> pageMerchant(ProductPageQueryDTO productPageQueryDTO) {
        log.info("page query for merchant:{}", productPageQueryDTO);
        PageResult pageResult = productService.pageQueryMerchant(productPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * modify product
     *
     * @param productDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "modify product")
    public Result update(@RequestBody ProductDTO productDTO) {
        log.info("modify product：{}", productDTO);
        productService.update(productDTO);

        return Result.success();
    }

    /**
     * delete product
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "delete product")
    public Result<String> deleteById(Long id){
        log.info("delete product：{}", id);
        productService.deleteById(id);
        return Result.success();
    }


}
