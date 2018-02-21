package win.yayuanzi8.thirdpartyservice.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import win.yayuanzi8.common.response.RestfulResponse;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class ReconizeResponse extends RestfulResponse {
    @JsonProperty("content")
    private final String content;

    public ReconizeResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
