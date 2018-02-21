package win.yayuanzi8.noteservice.service;


import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.domain.ShareNote;
import win.yayuanzi8.noteservice.model.NoteView;

import java.util.Set;

public interface ShareNoteService {
    ShareNote share(String nid, Integer uid);

    Set<NoteView> getMyShare(Integer uid);

    Note getShare(String sid);

    Integer cancelShare(String sid, Integer uid);
}
