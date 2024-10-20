package com.nusiss.productservice.client;

import com.nusiss.productservice.config.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "inventory-service",contextId="inventoryfeignzm1")
public interface InventoryClient {

    @PutMapping("/inventory/update")
    int update(@RequestHeader("authToken") String authToken, @RequestParam("productId") Long productId, @RequestParam("availableStock") int availableStock);

    @PostMapping("/inventory")
    ApiResponse<String> add(@RequestHeader("authToken") String authToken, @RequestParam("productId") Long productId, @RequestParam("availableStock") int availableStock);

    @GetMapping("/inventory")
    int get(Long productId);

    @DeleteMapping("/inventory")
    int delete(Long productId);


}
