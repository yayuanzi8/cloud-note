package win.yayuanzi8.noteservice.config;

import com.google.common.base.Strings;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
import win.yayuanzi8.noteservice.aspect.RequireRemoteAuthAspect;
import win.yayuanzi8.noteservice.service.RemoteAuthServiceFallBack;


import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig implements ServletRequestListener {

    private HttpServletRequest request;

    @Bean
    public RequireRemoteAuthAspect requireRemoteAuthAspect(RemoteAuthServiceFallBack remoteAuthServiceFallBack){
        return new RequireRemoteAuthAspect();
    }

    @Bean
    public RemoteAuthServiceFallBack remoteAuthServiceFallBack(){
        return new RemoteAuthServiceFallBack();
    }

    //Feign配置Authorization头
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = request.getHeader("Authorization");
            if (!Strings.isNullOrEmpty(token)) {
                requestTemplate.header("Authorization", token);
            }
        };
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        this.request = (HttpServletRequest) servletRequestEvent.getServletRequest();
    }
}
