package win.yayuanzi8.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.userservice.aspect.RequireAuth;
import win.yayuanzi8.userservice.domain.User;
import win.yayuanzi8.userservice.model.request.ChangeEmailRequest;
import win.yayuanzi8.userservice.model.request.ChangePortraitRequest;
import win.yayuanzi8.userservice.model.request.ChangePswRequest;
import win.yayuanzi8.userservice.model.request.SendEmailRequest;
import win.yayuanzi8.userservice.model.response.ChangeEmailResponse;
import win.yayuanzi8.userservice.model.response.ChangePortraitResponse;
import win.yayuanzi8.userservice.model.response.ChangePswResponse;
import win.yayuanzi8.userservice.model.response.UserInfoResponse;
import win.yayuanzi8.userservice.service.UserService;
import win.yayuanzi8.userservice.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/uploadPortrait")
    public Response uploadPortrait(@RequestPart("image") MultipartFile portraitFile) {
        return userService.uploadPortrait(portraitFile);
    }

    @RequireAuth
    @GetMapping("/loginUserInfo")
    public Response getLoginUserInfo(HttpServletRequest request) {
        Integer uid = jwtTokenUtil.getUidFromToken(request.getHeader(this.header));
        User user = userService.loadUserInfo(uid);
        return new UserInfoResponse(user.getUid(), user.getUsername(), user.getEmail(), user.getPortrait(), user.getRegisterTime());
    }

    @PostMapping("/sendRegisterEmail")
    public Response sendRegisterEmail(@RequestBody SendEmailRequest sendEmailRequest) {
        return userService.sendRegisterEmail(sendEmailRequest);
    }

    @RequireAuth
    @PatchMapping("/changePsw")
    public Response changePsw(@RequestBody ChangePswRequest cpRequest, HttpServletRequest request) {
        Integer uid = jwtTokenUtil.getUidFromToken(request.getHeader(this.header));
        userService.changePsw(uid, cpRequest.getOldPsw(), cpRequest.getNewPsw());
        return new ChangePswResponse();
    }

    @RequireAuth
    @PatchMapping("/changeEmail")
    public Response changeEmail(@RequestBody ChangeEmailRequest ceRequest, HttpServletRequest request) {
        Integer uid = jwtTokenUtil.getUidFromToken(request.getHeader(this.header));
        userService.changeEmail(uid, ceRequest.getEmail(), ceRequest.getCode());
        return new ChangeEmailResponse();
    }

    @RequireAuth
    @PatchMapping("/changePortrait")
    public Response changePortrait(@RequestBody ChangePortraitRequest cpRequest, HttpServletRequest request) {
        Integer uid = jwtTokenUtil.getUidFromToken(request.getHeader(this.header));
        userService.changePortrait(uid, cpRequest.getPortrait());
        return new ChangePortraitResponse();
    }

}
