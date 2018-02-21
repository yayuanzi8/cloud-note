package win.yayuanzi8.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.userservice.controller.StatusCode;
import win.yayuanzi8.userservice.dao.UserRepository;
import win.yayuanzi8.userservice.domain.User;
import win.yayuanzi8.userservice.model.request.RegisterRequest;
import win.yayuanzi8.userservice.model.response.JwtAuthResponse;
import win.yayuanzi8.userservice.model.response.RegisterResponse;
import win.yayuanzi8.userservice.util.JwtTokenUtil;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.expiration}")
    private long expDuration;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder, StringRedisTemplate stringRedisTemplate) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public RegisterResponse register(final RegisterRequest registerRequest) {
        checkRegisterInfo(registerRequest);
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setRegisterTime(new Date());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPortrait(registerRequest.getPortrait());
        userRepository.saveAndFlush(user);
        final RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUid(user.getUid());
        registerResponse.setUsername(user.getUsername());
        registerResponse.setEmail(user.getEmail());
        registerResponse.setPortrait(user.getPortrait());
        registerResponse.setRegisterTime(user.getRegisterTime());
        return registerResponse;
    }

    private void checkRegisterInfo(RegisterRequest registerRequest) {
        String storeEmailCode = stringRedisTemplate.opsForValue().get(registerRequest.getEmail());
        if (storeEmailCode == null) {
            Shift.fatal(StatusCode.EMAIL_CODE_NOT_FOUND);
        }
        if (!storeEmailCode.equalsIgnoreCase(registerRequest.getCode())) {
            Shift.fatal(StatusCode.EMAIL_CODE_ERROR);
        }
        int unLength = registerRequest.getUsername().trim().length();
        if (unLength > 20 || unLength < 2) {
            Shift.fatal(StatusCode.UN_LENGTH_ERROR);
        }
        User registeredUser = userRepository.findByUsername(registerRequest.getUsername().trim());
        if (registeredUser != null) {
            Shift.fatal(StatusCode.USER_EXISTS);
        }
        int pwLength = registerRequest.getPassword().trim().length();
        if (pwLength == 0 || pwLength > 50) {
            Shift.fatal(StatusCode.PWD_LENGTH_ERROR);
        }
        if (userRepository.countByEmail(registerRequest.getEmail()) != 0) {
            Shift.fatal(StatusCode.EMAIL_EXISTS);
        }
    }

    @Override
    public JwtAuthResponse login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken =
                new UsernamePasswordAuthenticationToken(username.trim(), password.trim());
        authenticationManager.authenticate(upToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtAuthResponse(token,expDuration , userDetails);
    }

    @Override
    public String refresh(String oldToken) {
        if (jwtTokenUtil.canTokenBeRefreshed(oldToken))
            return jwtTokenUtil.refreshToken(oldToken);
        return null;
    }

    @Override
    public String logout(String token) {
        return jwtTokenUtil.expireToken(token);
    }
}
