package win.yayuanzi8.thirdpartyservice.config;

import com.baidu.aip.speech.AipSpeech;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeechConfig {
    @Value(value = "${baidu.speech.appId}")
    private String appId;
    @Value(value = "${baidu.speech.apiKey}")
    private String apiKey;
    @Value(value = "${baidu.speech.secretKey}")
    private String secretKey;

    @Bean
    public AipSpeech aipSpeech() {
        return new AipSpeech(appId, apiKey, secretKey);
    }
}
