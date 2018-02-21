package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import win.yayuanzi8.common.response.RestfulResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class DeleteResponse extends RestfulResponse {
    @JsonProperty("changeNum")
    private final Integer changeNum;

    public DeleteResponse(Integer changeNum) {
        this.changeNum = changeNum;
    }

    public Integer getChangeNum() {
        return changeNum;
    }
}
