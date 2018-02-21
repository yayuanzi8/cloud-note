package win.yayuanzi8.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.userservice.controller.StatusCode;
import win.yayuanzi8.userservice.dao.UserRepository;
import win.yayuanzi8.userservice.domain.User;
import win.yayuanzi8.userservice.model.request.SendEmailRequest;
import win.yayuanzi8.userservice.model.response.SendEmailResponse;
import win.yayuanzi8.userservice.model.response.UploadResponse;
import win.yayuanzi8.userservice.util.CaptchaUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final MailService mailService;
    private final TemplateEngine templateEngine;
    private final CaptchaUtil captchaGenerator;
    private final ExecutorService service;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           StringRedisTemplate stringRedisTemplate,
                           MailService mailService,
                           TemplateEngine templateEngine,
                           CaptchaUtil captchaGenerator,
                           ExecutorService service, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.mailService = mailService;
        this.templateEngine = templateEngine;
        this.captchaGenerator = captchaGenerator;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SendEmailResponse sendRegisterEmail(SendEmailRequest sendEmailRequest) {
        int usedNumOfEmail = userRepository.countByEmail(sendEmailRequest.getEmail());
        if (usedNumOfEmail != 0) {
            Shift.fatal(StatusCode.EMAIL_EXISTS);
        }
        sendEmail(sendEmailRequest.getEmail());
        SendEmailResponse sendEmailResponse = new SendEmailResponse();
        sendEmailResponse.setCode(20000);
        sendEmailResponse.setMsg("已发送邮件到:" + sendEmailRequest.getEmail());
        return sendEmailResponse;
    }

    private void sendEmail(String email) {
        try {
            Context context = new Context();
            String emailCode = new String(captchaGenerator.getValidateString());
            stringRedisTemplate.opsForValue().set(email, emailCode, 30, TimeUnit.MINUTES);
            context.setVariable("emailCode", emailCode);
            String templateContent = templateEngine.process("emailTemplate", context);
            service.execute(() -> mailService.sendHtmlEmail(email, "来自云笔记的注册邮件", templateContent));
        } catch (Throwable e) {
            Shift.fatal(StatusCode.SERVER_UNKNOWN_ERROR, e.getMessage());
        }
    }

    @Override
    public UploadResponse uploadPortrait(MultipartFile portraitFile) {
        return save(portraitFile);
    }

    @Override
    public User loadUserInfo(Integer uid) {
        return userRepository.findByUid(uid);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void changePsw(Integer uid, String oldPsw, String newPsw) {
        User user = userRepository.findByUid(uid);
        if (user != null) {
            boolean match = passwordEncoder.matches(oldPsw, user.getPassword());
            if (!match) {
                Shift.fatal(StatusCode.PSW_ERROR);
            }
            user.setPassword(passwordEncoder.encode(newPsw));
            userRepository.update(user.getEmail(), user.getPassword(), user.getPortrait(), user.getUid());
        } else {
            Shift.fatal(StatusCode.USER_NOT_EXISTS);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void changeEmail(Integer uid, String email, String code) {
        String emailCode = stringRedisTemplate.opsForValue().get(email);
        if (emailCode == null) {
            Shift.fatal(StatusCode.EMAIL_CODE_NOT_FOUND);
        }
        if (!emailCode.equalsIgnoreCase(code)) {
            Shift.fatal(StatusCode.EMAIL_CODE_ERROR);
        }
        User user = userRepository.findByUid(uid);
        user.setEmail(email);
        userRepository.update(user.getEmail(), user.getPassword(), user.getPortrait(), uid);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void changePortrait(Integer uid, String portrait) {
        User user = userRepository.findByUid(uid);
        if (user != null) {
            user.setPortrait(portrait);
            userRepository.update(user.getEmail(), user.getPassword(), user.getPortrait(), uid);
        } else {
            Shift.fatal(StatusCode.USER_NOT_EXISTS);
        }
    }

    private UploadResponse save(MultipartFile portraitFile) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setImgUrl("http://images.ioliu.cn/bing/KatenaLuminarium_EN-AU12074286571_1920x1080.jpg");
        uploadResponse.setImgName(portraitFile.getName());
        return uploadResponse;
    }
}
