package win.yayuanzi8.noteservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.domain.BaseFile;
import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.domain.Rubbish;
import win.yayuanzi8.noteservice.repository.DirectoryRepository;
import win.yayuanzi8.noteservice.repository.NoteRepository;
import win.yayuanzi8.noteservice.repository.RubbishRepository;
import win.yayuanzi8.noteservice.service.RubbishService;
import win.yayuanzi8.noteservice.util.PathUtil;
import win.yayuanzi8.noteservice.util.StatusCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RubbishServiceImpl implements RubbishService {

    private final RubbishRepository rubbishRepository;
    private final NoteRepository noteRepository;
    private final DirectoryRepository directoryRepository;
    private final PathUtil pathUtil;

    @Autowired
    public RubbishServiceImpl(RubbishRepository rubbishRepository, NoteRepository noteRepository, DirectoryRepository directoryRepository, PathUtil pathUtil) {
        this.rubbishRepository = rubbishRepository;
        this.noteRepository = noteRepository;
        this.directoryRepository = directoryRepository;
        this.pathUtil = pathUtil;
    }

    @Override
    public List<Rubbish> getAllMyRubbish(Integer uid) {
        return rubbishRepository.findAllByUidAndRoot(uid, true);
    }

    @Override
    public List<BaseFile> recover(String rid, Integer uid) {
        Rubbish rubbish = rubbishRepository.findByUidAndRid(uid,rid);
        if (rubbish != null) {
            BaseFile file = rubbish.getFile();
            if ("directory".equals(file.getType())){
                Directory directory = (Directory) file;
                int index = directory.getPath().lastIndexOf(directory.getDirName());
                String parentPath = directory.getPath().substring(0, index - 1);
                boolean exist = pathUtil.existsDirInThisLevel(parentPath, uid, directory.getDirName());
                if (exist){
                    Shift.fatal(StatusCode.DUPLICATE_DIR_NAME);
                }
            }else{
                Note note = (Note) file;
                int index = note.getPath().lastIndexOf(note.getTitle());
                String parentPath = note.getPath().substring(0, index - 1);
                boolean exist = pathUtil.existsDirInThisLevel(parentPath, uid, note.getTitle());
                if (exist){
                    Shift.fatal(StatusCode.DUPLICATE_NOTE_NAME);
                }
            }
            String path = file.getPath();
            List<Rubbish> rubbishes = rubbishRepository.findAllByUidAndPath(uid, path);
            List<Note> notes = new LinkedList<>();
            List<Directory> directories = new LinkedList<>();
            for (Rubbish r : rubbishes) {
                BaseFile tempFile = r.getFile();
                if ("note".equals(tempFile.getType())) {
                    Note note = (Note)tempFile;
                    note.setNid(null);
                    notes.add(note);
                } else if ("directory".equals(tempFile.getType())) {
                    Directory directory = (Directory) tempFile;
                    directory.setDid(null);
                    directories.add(directory);
                }
            }
            List<Directory> insertedDirectories = directoryRepository.insert(directories);
            List<Note> insertedNotes = noteRepository.insert(notes);
            List<BaseFile> inserted = new ArrayList<>(insertedDirectories.size() + insertedNotes.size());
            inserted.addAll(insertedDirectories);
            inserted.addAll(insertedNotes);

            rubbishRepository.delete(rid);
            return inserted;
        }
        return new ArrayList<>();
    }
}
