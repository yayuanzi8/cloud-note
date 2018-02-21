package win.yayuanzi8.userservice.service;

import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.userservice.domain.User;
import win.yayuanzi8.userservice.model.request.SendEmailRequest;
import win.yayuanzi8.userservice.model.response.SendEmailResponse;
import win.yayuanzi8.userservice.model.response.UploadResponse;

public interface UserService {

    SendEmailResponse sendRegisterEmail(SendEmailRequest sendEmailRequest);

    UploadResponse uploadPortrait(MultipartFile portraitFile);

    User loadUserInfo(Integer uid);

    void changePsw(Integer uid, String oldPsw, String newPsw);

    void changeEmail(Integer uid, String email, String code);

    void changePortrait(Integer uid, String portrait);
}
