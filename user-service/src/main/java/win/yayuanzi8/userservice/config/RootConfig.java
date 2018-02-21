package win.yayuanzi8.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import win.yayuanzi8.userservice.aspect.RequireAuthAspect;
import win.yayuanzi8.userservice.util.JwtTokenUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableTransactionManagement
public class RootConfig {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public RequireAuthAspect requireAuthAspect(JwtTokenUtil jwtTokenUtil){
        return new RequireAuthAspect(jwtTokenUtil);
    }
}
