package win.yayuanzi8.noteservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.repository.DirectoryRepository;
import win.yayuanzi8.noteservice.repository.NoteRepository;
import win.yayuanzi8.noteservice.service.NoteService;
import win.yayuanzi8.noteservice.util.PathUtil;
import win.yayuanzi8.noteservice.util.StatusCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final DirectoryRepository directoryRepository;
    private final PathUtil pathUtil;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, DirectoryRepository directoryRepository, PathUtil pathUtil) {
        this.noteRepository = noteRepository;
        this.directoryRepository = directoryRepository;
        this.pathUtil = pathUtil;
    }


    @Override
    public Note createNewNote(String title, String parent, Integer uid) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent("");
        note.setCreateTime(new Date());
        note.setType("note");
        note.setUid(uid);
        Directory parentDir = directoryRepository.findByUidAndDid(uid, parent);
        String parentPath = pathUtil.getDirPath(parentDir,"");
        if (pathUtil.existsNoteInThisLevel(parentPath,uid, title)){
            Shift.fatal(StatusCode.DUPLICATE_NOTE_NAME);
        }
        note.setPath(parentPath + pathUtil.getPathSeperator() + title);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String nid, String title, String content, Integer uid) {
        Note note = noteRepository.findByUidAndNid(uid, nid);
        note.setTitle(title);
        note.setContent(content);
        return noteRepository.updateNote(note);
    }

    @Override
    public Integer rename(String nid, String newName, Integer uid) {
        Note note = noteRepository.findByUidAndNid(uid, nid);
        if (note == null){
            Shift.fatal(StatusCode.NOTE_NOT_EXIST);
        }
        int index = note.getPath().lastIndexOf(note.getTitle());
        String parentPath = note.getPath().substring(0, index - 1);
        if (pathUtil.existsNoteInThisLevel(parentPath, uid, note.getTitle())){
            Shift.fatal(StatusCode.DUPLICATE_NOTE_NAME);
        }
        note.setTitle(newName);
        note.setPath(parentPath + newName);
        Integer changeNum = 0;
        List<Note> noteList = new ArrayList<>();
        noteList.add(note);
        changeNum += noteRepository.batchUpdate(uid, noteList);
        return changeNum;
    }
}
