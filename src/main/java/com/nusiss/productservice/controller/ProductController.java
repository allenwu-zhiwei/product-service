package com.nusiss.productservice.controller;

import com.nusiss.productservice.constant.MessageConstant;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.domain.entity.ProductImage;
import com.nusiss.productservice.result.PageApiResponse;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "product info" , description = "These APIs for merchant and consumer, consumer only use pageConsumer method.")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5000")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * add product
     * @param productDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "add product")
    public ApiResponse save(@RequestHeader("authToken") String authToken, @RequestBody ProductDTO productDTO) {
        log.info("add product：{}", productDTO);
        productService.save(authToken, productDTO);

        return ApiResponse.success();
    }

    /**
     * page query for consumer
     * @param productPageQueryDTO
     * @return
     */
    @GetMapping("/page/consumer")
    @Operation(summary = "page query for consumer", description ="support query by product name(%), description(%), categoryId(=)")
    public ApiResponse<PageApiResponse> pageConsumer(ProductPageQueryDTO productPageQueryDTO) {
        log.info("page query for consumer:{}", productPageQueryDTO);
        PageApiResponse pageApiResponse = productService.pageQueryConsumer(productPageQueryDTO);
        return ApiResponse.success(pageApiResponse);
    }

    /**
     * page query merchant
     * @param productPageQueryDTO
     * @return
     */
    @GetMapping("/page/merchant")
    @Operation(summary = "page query for merchant", description ="support query by product name(%), categoryId(=), only query products that they create ")
    public ApiResponse<PageApiResponse> pageMerchant(@RequestHeader("authToken") String authToken, ProductPageQueryDTO productPageQueryDTO) {
        log.info("page query for merchant:{}", productPageQueryDTO);
        PageApiResponse pageApiResponse = productService.pageQueryMerchant(authToken, productPageQueryDTO);
        return ApiResponse.success(pageApiResponse);
    }

    /**
     * query productInfo by productId
     * @param productId
     * @return
     */
    @GetMapping
    @Operation(summary = "query productInfo by productId")
    public ApiResponse queryById(Long productId) {
        log.info("query productInfo by productId:{}", productId);
        ProductDTO product = productService.queryById(productId);
        return ApiResponse.success(product);
    }

    /**
     * modify product
     *
     * @param productDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "modify product", description = "modify should give backend all the image urls, it will delete the origin urls and insert new urls")
    public ApiResponse update(@RequestHeader("authToken") String authToken, @RequestBody ProductDTO productDTO) {
        log.info("modify product：{}", productDTO);
        productService.update(authToken, productDTO);

        return ApiResponse.success();
    }

    /**
     * delete product
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "delete product")
    public ApiResponse<String> deleteById(Long id){
        log.info("delete product：{}", id);
        productService.deleteById(id);
        return ApiResponse.success();
    }

    /**
     * image upload
     * @param files
     * @return
     */
    @PostMapping(value = "/image", consumes = "multipart/form-data")
    @Operation(summary = "image upload")
    public ApiResponse<List<String>> upload(@RequestPart MultipartFile[] files) {
        log.info("upload file info：{}", files);
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String filePath = productService.upload(file);
            if(StringUtils.isEmpty(filePath)) {
                return ApiResponse.error(MessageConstant.UPLOAD_FAILED);
            }
            fileUrls.add(filePath);
        }
        return ApiResponse.success(fileUrls);
    }

    /**
     * image delete
     * @param file
     * @return
     */
    @DeleteMapping("/image")
    @Operation(summary = "image delete", description = "used for update product operation when users delete exist image. This delete will delete the image from server")
    public ApiResponse<String> delete(String file) {
        log.info("delete file info：{}", file);
        boolean deleteRes = productService.deleteFile(file);
        if(deleteRes) {
            return ApiResponse.success();
        }

        return ApiResponse.error(MessageConstant.DELETE_FAILED);
    }

}
