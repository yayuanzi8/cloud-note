package win.yayuanzi8.noteservice.repository;


import win.yayuanzi8.noteservice.domain.Note;

import java.util.List;
import java.util.Set;

public interface NoteOperations {
    Note updateNote(Note note);

    int batchDeleteByNid(Integer uid, Set<String> noteIds);

    int batchDeleteByPath(Set<String> regexSet,Integer uid);

    List<Note> batchQuery(Integer uid, Set<String> regexSet);

    Integer batchUpdate(Integer uid, List<Note> noteWillBeUpdated);
}
