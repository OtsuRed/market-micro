package top.otsuland.product.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {
    @GetMapping("/check/{userId}")
    boolean checkUserExistence(@PathVariable("userId") Integer uid);

    @GetMapping("/getname/{userId}")
    String getUsername(@PathVariable("userId") Integer uid);

}
