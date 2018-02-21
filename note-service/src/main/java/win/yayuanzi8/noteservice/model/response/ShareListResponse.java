package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import win.yayuanzi8.common.response.RestfulResponse;
import win.yayuanzi8.noteservice.model.NoteView;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class ShareListResponse extends RestfulResponse {

    @JsonProperty("shareList")
    private final Set<NoteView> shareList;

    public ShareListResponse(Set<NoteView> shareList) {
        this.shareList = shareList;
    }

    public Set<NoteView> getShareList() {
        return shareList;
    }
}
