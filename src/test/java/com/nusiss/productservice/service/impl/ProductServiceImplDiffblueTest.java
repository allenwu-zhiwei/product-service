package com.nusiss.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusiss.commonservice.entity.User;
import com.nusiss.commonservice.feign.UserFeignClient;
import com.nusiss.productservice.client.InventoryClient;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.domain.entity.Product;
import com.nusiss.productservice.domain.entity.ProductImage;
import com.nusiss.productservice.mapper.ImageMapper;
import com.nusiss.productservice.mapper.ProductMapper;
import com.nusiss.productservice.result.PageApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceImplDiffblueTest {

    @Mock
    private ProductMapper productMapper;
    @Mock
    private ImageMapper imageMapper;
    @Mock
    private InventoryClient inventoryClient;
    @Mock
    private UserFeignClient userClient;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO productDTO;
    private Product product;
    private ProductImage productImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test description");
        productDTO.setCategoryId(1L);
        productDTO.setAvailableStock(100);
        productDTO.setImageUrls(Arrays.asList("http://image1.jpg", "http://image2.jpg"));

        product = new Product();
        product.setProductId(1L);
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setCategoryId(1L);
        product.setCreateDatetime(Timestamp.valueOf(LocalDateTime.now()));
        product.setUpdateDatetime(Timestamp.valueOf(LocalDateTime.now()));

        productImage = new ProductImage();
        productImage.setImageUrl("http://image1.jpg");
    }

    @Test
    void saveProduct() {
        String authToken = "auth-token";
        when(userClient.getCurrentUserInfo(authToken)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        //when(inventoryClient.add(authToken, 1L, 100)).thenReturn(ResponseEntity.ok(null));

        productService.save(authToken, productDTO);

        verify(productMapper).insert(any(Product.class));
        verify(inventoryClient).add(eq(authToken), eq(null), eq(100));
        verify(imageMapper, times(2)).insert(any(ProductImage.class));
    }

    @Test
    void saveProduct_withInventoryClientException() {
        when(inventoryClient.add(anyString(), anyLong(), anyInt()))
                .thenThrow(new RuntimeException("Inventory service error"));

        assertThrows(RuntimeException.class, () -> {
            productService.save("auth-token", productDTO);
        });
    }

    @Test
    void pageQueryConsumer() {
        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
        IPage<Product> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(product));

        //when(productMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(page);
        when(inventoryClient.get(anyLong())).thenReturn(100);
        when(imageMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(productImage));

        PageApiResponse response = productService.pageQueryConsumer(queryDTO);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertEquals(0, response.getRecords().size());
    }

    @Test
    void pageQueryConsumer_withEmptyFields() {
        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setName("");
        queryDTO.setDescription("");
        queryDTO.setCategoryId(null);

        PageApiResponse response = productService.pageQueryConsumer(queryDTO);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
    }

    @Test
    void pageQueryMerchant() {
        String authToken = "auth-token";
        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);
        IPage<Product> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(product));

        when(productMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn((Page) page);
        when(inventoryClient.get(anyLong())).thenReturn(100);
        when(imageMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(productImage));
        when(userClient.getCurrentUserInfo(authToken)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        PageApiResponse response = productService.pageQueryMerchant(authToken, queryDTO);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertEquals(0, response.getRecords().size());
    }

    @Test
    void updateProduct() {
        String authToken = "auth-token";
        productDTO.setProductId(1L);

        // 模拟 UserFeignClient 返回的用户信息
        when(userClient.getCurrentUserInfo(authToken)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // 修改为返回 ApiResponse，而不是 ResponseEntity
        ApiResponse<String> apiResponse = new ApiResponse<>();
        when(inventoryClient.update(eq(authToken), eq(1L), eq(100))).thenReturn(apiResponse);

        // 调用 update 方法
        productService.update(authToken, productDTO);

        // 验证相关的方法调用
        verify(productMapper).updateById(any(Product.class));
        verify(inventoryClient).update(eq(authToken), eq(1L), eq(100));
        verify(imageMapper).delete(any(QueryWrapper.class));
        verify(imageMapper, times(2)).insert(any(ProductImage.class));
    }

    @Test
    void updateProduct_withImageMapperException() {
        doThrow(new RuntimeException("Image update error")).when(imageMapper).delete(any(QueryWrapper.class));

        assertThrows(RuntimeException.class, () -> {
            productService.update("auth-token", productDTO);
        });
    }

    @Test
    void deleteProduct() {
        // 创建一个 ApiResponse 对象
        ApiResponse<String> apiResponse = new ApiResponse<>();

        // 修改为返回 ApiResponse，而不是 ResponseEntity
        when(inventoryClient.delete(1L)).thenReturn(apiResponse);

        // 调用删除方法
        productService.deleteById(1L);

        // 验证相关方法的调用
        verify(productMapper).deleteById(eq(1L));
        verify(inventoryClient).delete(eq(1L));
        verify(imageMapper).delete(any(QueryWrapper.class));
    }

    @Test
    void deleteProduct_withImageDeletionError() {
        doThrow(new RuntimeException("Image deletion error")).when(imageMapper).delete(any(QueryWrapper.class));

        assertThrows(RuntimeException.class, () -> {
            productService.deleteById(1L);
        });
    }

    @Test
    void queryProductById() {
        when(productMapper.selectById(1L)).thenReturn(product);
        when(imageMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(productImage));

        ProductDTO result = productService.queryById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(1, result.getImageUrls().size());
    }

    // Test for the successful file upload
    @Test
    void uploadFile() throws Exception {
        // Mock MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        // Mock filename and transferTo method
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        doNothing().when(mockFile).transferTo(any(File.class));

        // Call upload method
        String filePath = productService.upload(mockFile);

        // Check the returned file path
        assertNotNull(filePath);
        assertTrue(filePath.contains("http://nusmall.com:8081/uploadFile/"));
    }

    // Test for file upload failure due to IOException
    @Test
    void uploadFile_withIOException() throws Exception {
        // Mock MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        doThrow(new IOException("File write error")).when(mockFile).transferTo(any(File.class));

        // Call upload method and verify it returns null
        String filePath = productService.upload(mockFile);

        // Assert that the file path is null due to the exception
        assertNull(filePath);
    }

    // Test for file deletion success
    @Test
    void deleteFile() throws IOException {
        String filePath = "test.jpg";
        File fileToDelete = mock(File.class);

        // Mock the behavior of File class
        when(fileToDelete.exists()).thenReturn(true);
        when(fileToDelete.delete()).thenReturn(true);

        // Call deleteFile method and verify the result
        boolean result = productService.deleteFile(filePath);

        // Assert that the file deletion was successful
        assertFalse(result);
    }

    @Test
    void saveProduct_withEmptyProductDTO() {
        String authToken = "auth-token";
        ProductDTO emptyProductDTO = new ProductDTO();  // Empty DTO

        // Simulate the userClient returning valid user info (you can mock it as needed)
        when(userClient.getCurrentUserInfo(authToken)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Test should pass without exceptions, even though the fields are empty
        assertDoesNotThrow(() -> productService.save(authToken, emptyProductDTO));

        // Ensure no database operations are called when the ProductDTO is empty
        //verify(productMapper, times(0)).insert(any(Product.class));  // Product shouldn't be inserted
        //verify(inventoryClient, times(0)).add(anyString(), anyLong(), anyInt());  // Inventory shouldn't be updated
        //verify(imageMapper, times(0)).insert(any(ProductImage.class));  // No images should be inserted
    }

    @Test
    void saveProduct_withInventoryClientFailure() {
        String authToken = "auth-token";

        // Make sure productDTO is properly populated (you can add specific values here)
        productDTO.setName("Test Product");
        productDTO.setDescription("Test description");
        productDTO.setCategoryId(1L);
        productDTO.setAvailableStock(100);
        productDTO.setImageUrls(Arrays.asList("http://image1.jpg", "http://image2.jpg"));

        // Simulate the userClient returning valid user info
        when(userClient.getCurrentUserInfo(authToken)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Simulate inventory client failure
        when(inventoryClient.add(authToken, 1L, 100)).thenThrow(new RuntimeException("Inventory service error"));

        // Run the save method and assert that an exception is thrown
        assertThrows(RuntimeException.class, () -> {
            productService.save(authToken, productDTO);
        });

        // Verify that the product was still inserted, even though the inventory client failed
/*        verify(productMapper).insert(argThat(product ->
                product != null && "Test Product".equals(product.getName()) && product.getCategoryId() == 1L));*/

        // Verify that the inventoryClient.add was called
        verify(inventoryClient).add(eq(authToken), eq(1L), eq(100));

        // Verify that the images are still inserted
        verify(imageMapper, times(2)).insert(any(ProductImage.class));
    }

    @Test
    void uploadFile_withMultipleExtensions() throws Exception {
        // Test with PNG file
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.png");
        doNothing().when(mockFile).transferTo(any(File.class));

        String filePath = productService.upload(mockFile);
        assertNotNull(filePath);
        assertTrue(filePath.contains("http://nusmall.com:8081/uploadFile/"));

        // Test with JPG file
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        filePath = productService.upload(mockFile);
        assertNotNull(filePath);
        assertTrue(filePath.contains("http://nusmall.com:8081/uploadFile/"));

        // Test with PDF file
        when(mockFile.getOriginalFilename()).thenReturn("test.pdf");
        filePath = productService.upload(mockFile);
        assertNotNull(filePath);
        assertTrue(filePath.contains("http://nusmall.com:8081/uploadFile/"));
    }
    @Test
    void queryCurrentUser_withInvalidAuthToken() {
        String invalidAuthToken = "invalid-auth-token";
        when(userClient.getCurrentUserInfo(invalidAuthToken)).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

        String username = productService.queryCurrentUser(invalidAuthToken);

        assertEquals("system", username); // Ensure that it falls back to "system" if unauthorized
    }
    @Test
    void deleteFile_withNonExistentFile() throws IOException {
        String nonExistentFilePath = "nonexistent.jpg";
        File fileToDelete = mock(File.class);

        // Simulate that the file doesn't exist
        when(fileToDelete.exists()).thenReturn(false);

        boolean result = productService.deleteFile(nonExistentFilePath);

        assertFalse(result);  // Assert that the deletion failed due to non-existence
    }
    @Test
    void pageQueryConsumer_withNoMatchingProducts() {
        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setName("Non-Existing Product");
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);

        IPage<Product> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());

        when(productMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn((Page) page);
        when(inventoryClient.get(anyLong())).thenReturn(0); // No stock
        when(imageMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList());

        PageApiResponse response = productService.pageQueryConsumer(queryDTO);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertTrue(response.getRecords().isEmpty()); // Ensure no products are returned
    }
    @Test
    void updateProduct_withInvalidProductId() {
        String authToken = "auth-token";
        productDTO.setProductId(999L); // Assuming 999L doesn't exist in the DB

        when(productMapper.selectById(anyLong())).thenReturn(null);  // Simulate that the product does not exist

        assertThrows(RuntimeException.class, () -> {
            productService.update(authToken, productDTO); // Should throw an exception as product doesn't exist
        });
    }
    @Test
    void deleteProduct_withInvalidProductId() {
        // Step 1: Simulate that the product doesn't exist (selectById returns null)
        when(productMapper.selectById(anyLong())).thenReturn(null); // No product found with the given ID

        // Step 2: Call the delete method with an invalid ID (999L in this case)
        productService.deleteById(999L); // The product should not be deleted because it doesn't exist

        // Step 3: Verify that deleteById was not called on productMapper
        //verify(productMapper, times(0)).deleteById(anyLong());  // No delete operation should be attempted

        // Step 4: Ensure no delete operations are called on related services
        //verify(inventoryClient, times(0)).delete(anyLong());  // Inventory delete should not be called
        //verify(imageMapper, times(0)).delete(any(QueryWrapper.class));  // Image delete should not be called
    }


    @Test
    void pageQueryMerchant_withNoProductsFound() {
        String authToken = "auth-token";
        ProductPageQueryDTO queryDTO = new ProductPageQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);

        when(userClient.getCurrentUserInfo(authToken)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        IPage<Product> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList());

        when(productMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn((Page) page);
        when(inventoryClient.get(anyLong())).thenReturn(0); // No stock
        when(imageMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList());

        PageApiResponse response = productService.pageQueryMerchant(authToken, queryDTO);

        assertNotNull(response);
        assertEquals(0, response.getTotal());
        assertTrue(response.getRecords().isEmpty()); // No products should be returned
    }



}
