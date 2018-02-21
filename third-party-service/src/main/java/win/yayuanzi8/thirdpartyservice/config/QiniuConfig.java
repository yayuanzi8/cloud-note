package win.yayuanzi8.thirdpartyservice.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.UploadManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QiniuConfig {

    @Bean
    public UploadManager uploadManager() {
        com.qiniu.storage.Configuration qiniuConf = new com.qiniu.storage.Configuration(Zone.zone2());
        return new UploadManager(qiniuConf);
    }
}
