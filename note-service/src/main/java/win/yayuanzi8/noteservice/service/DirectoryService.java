package win.yayuanzi8.noteservice.service;

import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.model.request.NewDirRequest;
import win.yayuanzi8.noteservice.model.response.GetUserDirListResponse;

import java.util.List;

public interface DirectoryService {
    GetUserDirListResponse findUserDirNameList(Integer uid, String did);

    Directory createNewDir(NewDirRequest request, Integer uid);

    Integer batchDeleteDirAndNote(List<String> dirs, List<String> notes, Integer uid);

    List<Directory> findUserDirList(Integer uid, String did);

    Integer moveDirAndNote(List<String> dirs, List<String> notes, String dest, Integer uid);

    Integer copyDirAndNote(List<String> dirs, List<String> notes, String dest, Integer uid);

    Integer moveToRubbish(List<String> dirs, List<String> notes, Integer uid);

    Integer rename(String did, String newName, Integer uid);
}
