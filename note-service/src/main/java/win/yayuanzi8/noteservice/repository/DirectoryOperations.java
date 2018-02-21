package win.yayuanzi8.noteservice.repository;

import win.yayuanzi8.noteservice.domain.Directory;

import java.util.List;
import java.util.Set;

public interface DirectoryOperations {
    Integer batchDeleteByPath(Set<String> regexSet,Integer uid);

    List<Directory> batchQuery(Integer uid, Set<String> regexSet);

    Integer batchUpdate(Integer uid, List<Directory> dirWillBeUpdated);
}
