package win.yayuanzi8.userservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import win.yayuanzi8.common.response.ErrorEntity;
import win.yayuanzi8.userservice.controller.StatusCode;
import win.yayuanzi8.userservice.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class RequireAuthAspect {

    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RequireAuthAspect(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Around(value = "@annotation(win.yayuanzi8.userservice.aspect.RequireAuth)")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(this.header);
        if (token != null && token.startsWith(tokenHead)) {
            boolean success = jwtTokenUtil.validateToken(token);
            if (success)
                return joinPoint.proceed();
        }
        return new ErrorEntity(StatusCode.AUTH_FAIL);
    }
}
