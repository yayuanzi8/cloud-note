package win.yayuanzi8.thirdpartyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ThirdPartyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyServiceApplication.class, args);
    }
}
