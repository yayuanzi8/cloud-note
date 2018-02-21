package win.yayuanzi8.noteservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.repository.DirectoryRepository;
import win.yayuanzi8.noteservice.repository.NoteRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yayua
 */
@Component
public class PathUtil {

    private final DirectoryRepository directoryRepository;
    private final NoteRepository noteRepository;

    @Value("${path.regex}")
    private String pathRegex;
    @Value("${path.seperator}")
    private String pathSeperator;

    @Autowired
    public PathUtil(DirectoryRepository directoryRepository, NoteRepository noteRepository) {
        this.directoryRepository = directoryRepository;
        this.noteRepository = noteRepository;
    }

    public String generatePath(String parentPath, String dirName) {
        return parentPath + dirName;
    }

    public boolean existsDirInThisLevel(String parentPath, Integer uid, String dirName) {
        String regex = "^" + parentPath + getPathSeperator() + getPathRegex() + "$";
        Set<String> regexSet = new HashSet<>();
        regexSet.add(regex);
        List<Directory> directoryList = directoryRepository.batchQuery(uid, regexSet);
        for (Directory d : directoryList) {
            if (d.getDirName().equals(dirName)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsNoteInThisLevel(String parentPath, Integer uid, String title) {
        String regex = "^" + parentPath + getPathSeperator() + getPathRegex() + "$";
        Set<String> regexSet = new HashSet<>();
        regexSet.add(regex);
        List<Note> notes = noteRepository.batchQuery(uid, regexSet);
        for (Note note : notes) {
            if (note.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public String getDirPath(Directory directory, String defaultValue) {
        if (directory != null) {
            return directory.getPath();
        }
        return defaultValue;
    }

    public String getPathRegex() {
        return pathRegex;
    }

    public String getPathSeperator() {
        return pathSeperator;
    }

    public void checkDuplicateDirInOneDir(List<Directory> directoryList, List<Directory> destDirList){
        for (Directory d1 : directoryList) {
            for (Directory d2 : destDirList) {
                if (d1.getDirName().equals(d2.getDirName())){
                    Shift.fatal(StatusCode.DUPLICATE_DIR_NAME);
                }
            }
        }
    }

    public void checkDuplicateNoteInOneDir(List<Note> list1,List<Note> list2){
        for (Note n1 : list1) {
            for (Note n2 : list2) {
                if (n1.getTitle().equals(n2.getTitle())){
                    Shift.fatal(StatusCode.DUPLICATE_NOTE_NAME);
                }
            }
        }
    }
}
