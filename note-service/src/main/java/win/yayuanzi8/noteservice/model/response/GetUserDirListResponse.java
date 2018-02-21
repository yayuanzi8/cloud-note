package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import win.yayuanzi8.common.response.RestfulResponse;
import win.yayuanzi8.noteservice.domain.BaseFile;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class GetUserDirListResponse extends RestfulResponse {
    @JsonProperty("fileSet")
    private Set<BaseFile> fileSet;

    public void setFileSet(Set<BaseFile> fileSet) {
        this.fileSet = fileSet;
    }

    public Set<BaseFile> getFileSet() {
        return fileSet;
    }
}
