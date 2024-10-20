package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nusiss.commonservice.feign.UserFeignClient;
import com.nusiss.productservice.client.InventoryClient;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.domain.entity.ProductImage;
import com.nusiss.productservice.mapper.ImageMapper;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.result.PageApiResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.nusiss.commonservice.entity.User;
import com.nusiss.commonservice.config.ApiResponse;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
//@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private UserFeignClient userClient;


    // images upload directory
    private final String UPLOAD_DIR = "static";

    /**
     * add product
     * @param productDTO
     */
    @Override
    public void save(String authToken, ProductDTO productDTO) {

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        product.setCreateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        product.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        // user info

        product.setCreateUser(queryCurrentUser(authToken));
        product.setUpdateUser(queryCurrentUser(authToken));

        productMapper.insert(product);

        // inventory info
        inventoryClient.add(authToken, product.getProductId(), productDTO.getAvailableStock());

        // image info

        List<String> imageUrls = productDTO.getImageUrls();
        if(imageUrls != null && imageUrls.size() > 0){
            for(String url : imageUrls) {
                ProductImage image = new ProductImage();
                image.setCreateUser(queryCurrentUser(authToken));
                image.setUpdateUser(queryCurrentUser(authToken));
                image.setProductId(product.getProductId());
                image.setImageUrl(url);
                imageMapper.insert(image);
            }
        }
    }

    /**
     * page query for consumer
     * @param productPageQueryDTO
     * @return
     */
    @Override
    public PageApiResponse pageQueryConsumer(ProductPageQueryDTO productPageQueryDTO) {

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
        //get images
        List<Product> products = page.getRecords();
        QueryWrapper<ProductImage> imageQueryWrapper = new QueryWrapper<>();
        for(Product product : products){
            imageQueryWrapper.eq("product_id", productPageQueryDTO.getProductId());
            List<ProductImage> imageList = imageMapper.selectList(imageQueryWrapper);
            product.setProductImages(imageList);
        }

        return new PageApiResponse(page.getTotal(), page.getRecords());
    }

    /**
     * page query for merchant
     * @param productPageQueryDTO
     * @return
     */
    @Override
    public PageApiResponse pageQueryMerchant(String authToken, ProductPageQueryDTO productPageQueryDTO) {

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
        // only query themselves created product
        queryWrapper.eq("create_user", queryCurrentUser(authToken));


        productMapper.selectPage(page, queryWrapper);
        //get inventory
        List<Product> products = page.getRecords();
        for(Product product : products){
            int stock = inventoryClient.get(product.getProductId());
            product.setAvailableStock(stock);
        }
        // get images
        QueryWrapper<ProductImage> imageQueryWrapper = new QueryWrapper<>();
        for(Product product : products){
            imageQueryWrapper.eq("product_id", productPageQueryDTO.getProductId());
            List<ProductImage> imageList = imageMapper.selectList(imageQueryWrapper);
            product.setProductImages(imageList);
        }


        return new PageApiResponse(page.getTotal(), page.getRecords());
    }

    /**
     * modify product
     * @param productDTO
     */
    @Override
    public void update(String authToken, ProductDTO productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));

        //user info
        product.setUpdateUser(queryCurrentUser(authToken));
        productMapper.updateById(product);
        // inventory info
        inventoryClient.update(authToken, product.getProductId(), productDTO.getAvailableStock());
        // image info
        QueryWrapper<ProductImage> imageQueryWrapper = new QueryWrapper<>();
        imageQueryWrapper.eq("product_id", product.getProductId());
        // delete
        imageMapper.delete(imageQueryWrapper);
        // insert

        List<String> imageUrls = productDTO.getImageUrls();
        for(String url : imageUrls) {
            ProductImage image = new ProductImage();
            image.setCreateUser(queryCurrentUser(authToken));
            image.setUpdateUser(queryCurrentUser(authToken));
            image.setProductId(productDTO.getProductId());
            image.setImageUrl(url);
            imageMapper.insert(image);
        }

    }

    /**
     * delete product
     * @param id
     */
    @Override
    public void deleteById(Long id) {

        productMapper.deleteById(id);
        // inventory info
        inventoryClient.delete(id);
    }

    /**
     * upload file
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        try {
            // create directory
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // original file name
            String originalFilename = file.getOriginalFilename();
            //  get the extension of original file
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // generate new file name
            String objectName = UUID.randomUUID().toString() + extension;

            // save
            File serverFile = new File(directory, objectName);
            file.transferTo(serverFile);
            // get relative path
            String filePath = UPLOAD_DIR + "/" + objectName;
            log.info("File upload successfully, file path：{}", filePath);

            return filePath;
        } catch (IOException e) {
            log.error("Error while uploading the file: {}", e.getMessage());
            return null;
        }
    }

    /**
     * file delete
     * @param filePath
     * @return
     */
    @Override
    public boolean deleteFile(String filePath) {
        try {
            // Create a File object for the specified path
            File fileToDelete = new File(filePath);

            // Check if the file exists
            if (fileToDelete.exists()) {
                // Attempt to delete the file
                boolean isDeleted = fileToDelete.delete();
                if (isDeleted) {
                    log.info("File deleted successfully: {}", filePath);
                    return true;
                }
                log.error("Failed to delete the file: {}", filePath);
                return false;
            } else {
                log.warn("File not found: {}", filePath);
                return false;
            }
        } catch (Exception e) {
            log.error("Error while deleting the file: {}", e.getMessage());
            return false;
        }
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






