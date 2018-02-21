package win.yayuanzi8.noteservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import win.yayuanzi8.common.response.RestfulResponse;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.model.NoteView;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class GetCommonShareResponse extends RestfulResponse {

    private final Note note;

    public GetCommonShareResponse(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
