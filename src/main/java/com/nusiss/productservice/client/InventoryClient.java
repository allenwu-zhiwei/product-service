package com.nusiss.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "inventory-service")
public interface InventoryClient {

    @PutMapping("/inventory/update")
    int update(@RequestParam("id") long id, @RequestParam("availableStock") int availableStock);

    @PostMapping("/inventory")
    int add(@RequestParam("id") long id, @RequestParam("availableStock") int availableStock);

    @GetMapping("/inventory")
    int get(long id);


}
