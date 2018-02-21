package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import win.yayuanzi8.common.response.RestfulResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class CopyResponse extends RestfulResponse {

    private final Integer changeNum;

    public CopyResponse(Integer changeNum) {
        this.changeNum = changeNum;
    }

    public Integer getChangeNum() {
        return changeNum;
    }
}
