package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.common.response.RestfulResponse;
import win.yayuanzi8.noteservice.domain.Note;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class UpdateNoteResponse extends RestfulResponse {
    @JsonProperty("note")
    private final Note note;

    public UpdateNoteResponse(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
