package win.yayuanzi8.noteservice.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import win.yayuanzi8.noteservice.model.response.RemoteAuthResponse;

@FeignClient(name = "user-service"/*,fallback = RemoteAuthServiceFallBack.class*/)
public interface RemoteAuthService {

    @GetMapping("/loginUserInfo")
    RemoteAuthResponse fetchLoginUserInfo();
}
