package win.yayuanzi8.noteservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import win.yayuanzi8.common.response.ErrorEntity;
import win.yayuanzi8.noteservice.model.response.RemoteAuthResponse;
import win.yayuanzi8.noteservice.service.RemoteAuthService;
import win.yayuanzi8.noteservice.util.StatusCode;

@Aspect
public class RequireRemoteAuthAspect {

    @Autowired
    private RemoteAuthService authService;

    @Around("@annotation(win.yayuanzi8.noteservice.aspect.RequireRemoteAuth)")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        RemoteAuthResponse result = authService.fetchLoginUserInfo();
        Object[] params = new Object[joinPoint.getArgs().length];
        System.arraycopy(joinPoint.getArgs(), 0, params, 0, joinPoint.getArgs().length);
        params[joinPoint.getArgs().length - 1] = result;
        if (StatusCode.OK.code() == result.getCode()) {
            return joinPoint.proceed(params);
        }
        return new ErrorEntity(StatusCode.AUTH_FAIL);
    }
}
