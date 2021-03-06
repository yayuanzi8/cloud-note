package win.yayuanzi8.thirdpartyservice.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import win.yayuanzi8.common.response.RestfulResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class QiniuResponse extends RestfulResponse {

    private final String url;

    public QiniuResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
