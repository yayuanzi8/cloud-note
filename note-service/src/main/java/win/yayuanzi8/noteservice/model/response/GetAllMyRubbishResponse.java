package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import win.yayuanzi8.common.response.RestfulResponse;
import win.yayuanzi8.noteservice.domain.Rubbish;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class GetAllMyRubbishResponse extends RestfulResponse {

    private final List<Rubbish> rubbishes;

    public GetAllMyRubbishResponse(List<Rubbish> rubbishes) {
        this.rubbishes = rubbishes;
    }

    public List<Rubbish> getRubbishes() {
        return rubbishes;
    }
}
