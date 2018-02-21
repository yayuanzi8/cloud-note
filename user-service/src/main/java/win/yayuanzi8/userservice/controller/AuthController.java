package win.yayuanzi8.userservice.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.userservice.aspect.RequireAuth;
import win.yayuanzi8.userservice.model.request.JwtAuthRequest;
import win.yayuanzi8.userservice.model.request.RegisterRequest;
import win.yayuanzi8.userservice.model.response.JwtAuthResponse;
import win.yayuanzi8.userservice.model.response.LogoutResponse;
import win.yayuanzi8.userservice.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "用户注册",notes = "")
    @PostMapping("/auth/register")
    public Response register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @ApiOperation(value = "用户登录",notes = "")
    @PostMapping("/auth")
    public Response login(@RequestBody JwtAuthRequest jwtAuthRequest) {
         return authService.login(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());
    }

    @ApiOperation(value = "token刷新",notes = "")
    @RequireAuth
    @PostMapping("/refresh")
    public Response refresh(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String newToken = authService.refresh(token);
        return new JwtAuthResponse(newToken);
    }

    @ApiOperation(value = "注销",notes = "")
    @RequireAuth
    @PostMapping("/auth/logout")
    public Response logoutResponse(HttpServletRequest request) {
        LogoutResponse logoutResponse = new LogoutResponse();
        String token = request.getHeader(tokenHeader);
        try {
            logoutResponse.setToken(authService.logout(token));
            logoutResponse.setMsg("注销成功！");
        } catch (Throwable e) {
            logoutResponse.setMsg("注销失败！" + e.getLocalizedMessage());
        }
        return logoutResponse;
    }

}
