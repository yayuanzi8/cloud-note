package win.yayuanzi8.userservice.controller.advice;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import win.yayuanzi8.common.exception.RestStatusException;
import win.yayuanzi8.common.response.ErrorEntity;
import win.yayuanzi8.common.util.RestStatus;
import win.yayuanzi8.userservice.controller.StatusCode;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class FaultBarrier {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaultBarrier.class);
    private static final ImmutableMap<Class<? extends Throwable>, RestStatus> EXCEPTION_MAPPINGS;

    static {
        final ImmutableMap.Builder<Class<? extends Throwable>, RestStatus> builder = ImmutableMap.builder();
        // SpringMVC中参数类型转换异常，常见于String找不到对应的ENUM而抛出的异常
        builder.put(MethodArgumentTypeMismatchException.class, StatusCode.INVALID_PARAMS_CONVERSION);
        builder.put(UnsatisfiedServletRequestParameterException.class, StatusCode.INVALID_PARAMS_CONVERSION);
        // HTTP Request Method不存在
        builder.put(HttpRequestMethodNotSupportedException.class, StatusCode.REQUEST_METHOD_NOT_SUPPORTED);
        // 要求有RequestBody的地方却传入了NULL
        builder.put(HttpMessageNotReadableException.class, StatusCode.HTTP_MESSAGE_NOT_READABLE);
        // 通常是操作过快导致DuplicateKey
        builder.put(DuplicateKeyException.class, StatusCode.DUPLICATE_KEY);
        builder.put(BadCredentialsException.class,StatusCode.INVALID_CREDENTIAL);
        // 其他未被发现的异常
        builder.put(Exception.class, StatusCode.SERVER_UNKNOWN_ERROR);
        EXCEPTION_MAPPINGS = builder.build();
    }

    /**
     * <strong>Request域取出对应错误信息</strong>, 封装成实体ErrorEntity后转换成JSON输出
     *
     * @param e       {@code StatusCode}异常
     * @param request HttpServletRequest
     * @return ErrorEntity
     * @see ErrorEntity
     * @see StatusCode
     */
    @ResponseBody
    @ExceptionHandler(RestStatusException.class)
    public Object restStatusException(Exception e, HttpServletRequest request) {
        // 取出存储在Shift设定在Request Scope中的ErrorEntity
        return request.getAttribute(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorEntity exception(Exception e) {
        final RestStatus status = EXCEPTION_MAPPINGS.get(e.getClass());
        e.printStackTrace();
        final ErrorEntity error;
        if (status != null) {
            error = new ErrorEntity(status);
        } else {
            error = new ErrorEntity(StatusCode.SERVER_UNKNOWN_ERROR);
        }
        return error;
    }

}
