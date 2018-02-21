package win.yayuanzi8.noteservice.service;


import win.yayuanzi8.noteservice.model.response.RemoteAuthResponse;
import win.yayuanzi8.noteservice.util.StatusCode;

public class RemoteAuthServiceFallBack implements RemoteAuthService {
    @Override
    public RemoteAuthResponse fetchLoginUserInfo() {
        RemoteAuthResponse response = new RemoteAuthResponse();
        response.setCode(StatusCode.AUTH_FAIL.code());
        return response;
    }
}
