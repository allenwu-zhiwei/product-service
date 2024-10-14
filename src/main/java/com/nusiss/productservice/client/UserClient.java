package com.nusiss.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "user-service",contextId="userfeignzm1")
public interface UserClient {

    @GetMapping("/users/queryCurrentUser")
    String queryCurrentUser();
}
