package com.nusiss.productservice.client;

import com.nusiss.productservice.config.ApiResponse;
import org.apache.catalina.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "user-service",contextId="userfeignzm1")
public interface UserClient {

    @PostMapping("/getCurrentUserInfo")
    public ResponseEntity<ApiResponse<User>> getCurrentUserInfo(@RequestHeader("authToken") String authToken);
}
