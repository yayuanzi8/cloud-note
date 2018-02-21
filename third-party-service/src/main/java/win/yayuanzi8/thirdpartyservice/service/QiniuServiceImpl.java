package win.yayuanzi8.thirdpartyservice.service;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QiniuServiceImpl implements QiniuService {

    @Autowired
    private UploadManager uploadManager;

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.domainName}")
    private String domainName;

    @Override
    public String upload(byte[] fileBytes) {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(fileBytes, null, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key + ":" + putRet.hash);
            return "http://" + this.domainName + "/" + putRet.hash;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}
