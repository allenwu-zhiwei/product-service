package com.nusiss.productservice.controller;

import com.nusiss.productservice.constant.MessageConstant;
import com.nusiss.productservice.domain.dto.ProductDTO;
import com.nusiss.productservice.domain.dto.ProductPageQueryDTO;
import com.nusiss.productservice.result.PageApiResponse;
import com.nusiss.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testSaveProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");

        // Perform POST request
        mockMvc.perform(post("/product")
                        .header("authToken", "validAuthToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\"}"))
                .andExpect(status().isOk());

        // Verify that save method is called
        verify(productService, times(1)).save(eq("validAuthToken"), eq(productDTO));
    }

    @Test
    void testPageConsumer() throws Exception {
        ProductPageQueryDTO productPageQueryDTO = new ProductPageQueryDTO();
        //productPageQueryDTO.setProductName("Test Product");

        PageApiResponse pageApiResponse = new PageApiResponse();
        pageApiResponse.setTotal(1);
        //pageApiResponse.setItems(null);

        // Mock service method
        when(productService.pageQueryConsumer(any(ProductPageQueryDTO.class))).thenReturn(pageApiResponse);

        // Perform GET request
        mockMvc.perform(get("/product/page/consumer")
                        .param("productName", "Test Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1));

        // Verify that service method was called
        verify(productService, times(1)).pageQueryConsumer(eq(productPageQueryDTO));
    }

    @Test
    void testPageMerchant() throws Exception {
        ProductPageQueryDTO productPageQueryDTO = new ProductPageQueryDTO();
        //productPageQueryDTO.setProductName("Test Product");

        PageApiResponse pageApiResponse = new PageApiResponse();
        pageApiResponse.setTotal(1);
        //pageApiResponse.setItems(null);

        // Mock service method
        when(productService.pageQueryMerchant(any(), any(ProductPageQueryDTO.class))).thenReturn(pageApiResponse);

        // Perform GET request
        mockMvc.perform(get("/product/page/merchant")
                        .header("authToken", "validAuthToken")
                        .param("productName", "Test Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1));

        // Verify that service method was called
        verify(productService, times(1)).pageQueryMerchant(eq("validAuthToken"), eq(productPageQueryDTO));
    }

    @Test
    void testQueryById() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");  // 设置 productName 属性

        // Mock service method
        when(productService.queryById(1L)).thenReturn(productDTO);

        // Perform GET request
        mockMvc.perform(get("/product")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Product"));  // 使用正确的 JsonPath

        // Verify that service method was called
        verify(productService, times(1)).queryById(1L);
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setName("Updated Product");

        // Perform PUT request
        mockMvc.perform(put("/product")
                        .header("authToken", "validAuthToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"name\":\"Updated Product\"}"))
                .andExpect(status().isOk());

        // Verify that update method was called
        verify(productService, times(1)).update(eq("validAuthToken"), eq(productDTO));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long productId = 1L;

        // Perform DELETE request
        mockMvc.perform(delete("/product")
                        .param("id", String.valueOf(productId)))
                .andExpect(status().isOk());

        // Verify that delete method was called
        verify(productService, times(1)).deleteById(eq(productId));
    }

/*
    @Test
    void testUploadImage() throws Exception {
        // 模拟上传的文件内容
        String fileContent = "This is a test file content";
        byte[] fileBytes = fileContent.getBytes();

        // 执行文件上传
        mockMvc.perform(multipart("/product/image")  // multipart 请求
                        .file("files", fileBytes)  // 上传的文件内容
                        .contentType(MediaType.MULTIPART_FORM_DATA)  // 设置 content-type 为 multipart/form-data
                        .param("files", "file1.jpg"))  // 模拟上传文件的名称，可以根据需要设置
                .andExpect(status().isOk())  // 期望返回 200 OK
                .andExpect(jsonPath("$.data[0]").value("http://example.com/file1.jpg"));  // 校验返回的文件 URL

        // 验证服务方法是否被调用
        verify(productService, times(1)).upload(any(MultipartFile.class));
    }
*/





    @Test
    void testDeleteImage() throws Exception {
        String file = "http://example.com/file1.jpg";

        // Mock service method
        when(productService.deleteFile(file)).thenReturn(true);

        // Perform DELETE request for image deletion
        mockMvc.perform(delete("/product/image")
                        .param("file", file))
                .andExpect(status().isOk());

        // Verify that deleteFile method was called
        verify(productService, times(1)).deleteFile(eq(file));
    }

    @Test
    void testUploadImageSuccess() throws Exception {
        // 模拟上传的文件内容
        String fileContent = "This is a test file content";
        byte[] fileBytes = fileContent.getBytes();

        // 模拟文件上传成功后返回的文件 URL
        when(productService.upload(any(MultipartFile.class))).thenReturn("http://example.com/file1.jpg");

        // 执行文件上传
        mockMvc.perform(multipart("/product/image")  // multipart 请求
                        .file("files", fileBytes)  // 上传的文件内容
                        .contentType(MediaType.MULTIPART_FORM_DATA)  // 设置 content-type 为 multipart/form-data
                        .param("files", "file1.jpg"))  // 模拟上传文件的名称
                .andExpect(status().isOk())  // 期望返回 200 OK
                .andExpect(jsonPath("$.data[0]").value("http://example.com/file1.jpg"));  // 校验返回的文件 URL

        // 验证服务方法是否被调用
        verify(productService, times(1)).upload(any(MultipartFile.class));
    }

    @Test
    void testUploadImageFailure() throws Exception {
        // 模拟上传的文件内容
        String fileContent = "This is a test file content";
        byte[] fileBytes = fileContent.getBytes();

        // 模拟上传失败时返回错误信息
        when(productService.upload(any(MultipartFile.class))).thenReturn(null);

        // 执行文件上传
        mockMvc.perform(multipart("/product/image")  // multipart 请求
                        .file("files", fileBytes)  // 上传的文件内容
                        .contentType(MediaType.MULTIPART_FORM_DATA)  // 设置 content-type 为 multipart/form-data
                        .param("files", "file1.jpg"))  // 模拟上传文件的名称
                .andExpect(status().is2xxSuccessful())  // 期望返回 400 错误
                .andExpect(jsonPath("$.message").value(MessageConstant.UPLOAD_FAILED));  // 校验错误信息

        // 验证服务方法是否被调用
        verify(productService, times(1)).upload(any(MultipartFile.class));
    }

    @Test
    void testDeleteImageFailure() throws Exception {
        String file = "http://example.com/file1.jpg";

        // Mock service method: simulate file deletion failure
        when(productService.deleteFile(file)).thenReturn(false);

        // Perform DELETE request for image deletion
        mockMvc.perform(delete("/product/image")
                        .param("file", file))
                .andExpect(status().is2xxSuccessful())  // 期望返回 400 错误
                .andExpect(jsonPath("$.message").value(MessageConstant.DELETE_FAILED));  // 校验错误信息

        // Verify that deleteFile method was called
        verify(productService, times(1)).deleteFile(eq(file));
    }

    @Test
    void testQueryByIdProductNotFound() throws Exception {
        // Simulate a scenario where the product doesn't exist
        when(productService.queryById(999L)).thenReturn(null);

        // Perform GET request
        mockMvc.perform(get("/product")
                        .param("productId", "999"))
                .andExpect(status().is2xxSuccessful())  // 期望返回 404 错误
                .andExpect(jsonPath("$.message").value("success"));

        // Verify that service method was called
        verify(productService, times(1)).queryById(999L);
    }

/*    @Test
    void testDeleteProductFailure() throws Exception {
        Long productId = 999L;

        // Mock service method: simulate product deletion failure
        doThrow(new RuntimeException("Delete failed")).when(productService).deleteById(productId);

        // Perform DELETE request and expect 500 status with the appropriate error message in JSON body
        mockMvc.perform(delete("/product")
                        .param("id", String.valueOf(productId)))
                .andExpect(status().isInternalServerError())  // Expecting 500 error status
                .andExpect(jsonPath("$.message").value("Delete failed"));  // Expecting the correct error message

        // Verify that delete method was called exactly once
        verify(productService, times(1)).deleteById(eq(productId));
    }*/


}
