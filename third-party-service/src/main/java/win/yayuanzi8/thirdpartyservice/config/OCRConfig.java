package win.yayuanzi8.thirdpartyservice.config;

import com.baidu.aip.ocr.AipOcr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OCRConfig {

    @Value(value = "${baidu.ocr.appId}")
    private String appId;
    @Value(value = "${baidu.ocr.apiKey}")
    private String apiKey;
    @Value(value = "${baidu.ocr.secretKey}")
    private String secretKey;

    @Bean
    public AipOcr aipOcr() {
        return new AipOcr(appId, apiKey, secretKey);
    }
}
