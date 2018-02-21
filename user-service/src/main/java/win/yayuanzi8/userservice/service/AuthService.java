package win.yayuanzi8.userservice.service;

import win.yayuanzi8.userservice.domain.User;
import win.yayuanzi8.userservice.model.request.RegisterRequest;
import win.yayuanzi8.userservice.model.response.JwtAuthResponse;
import win.yayuanzi8.userservice.model.response.LoginResponse;
import win.yayuanzi8.userservice.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);

    JwtAuthResponse login(String username, String password);

    String refresh(String oldToken);

    String logout(String token);

}
