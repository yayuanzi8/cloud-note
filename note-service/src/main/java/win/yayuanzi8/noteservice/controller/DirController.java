package win.yayuanzi8.noteservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.aspect.RequireRemoteAuth;
import win.yayuanzi8.noteservice.domain.BaseFile;
import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.model.request.*;
import win.yayuanzi8.noteservice.model.response.*;
import win.yayuanzi8.noteservice.service.DirectoryService;
import win.yayuanzi8.noteservice.util.StatusCode;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author yayua
 */
@RestController
public class DirController {

    private final DirectoryService directoryService;

    @Autowired
    public DirController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @RequireRemoteAuth
    @GetMapping(value = {"/fileList/{did}"})
    public Response userFileList(@PathVariable(required = false) String did, RemoteAuthResponse response) {
        if (StatusCode.OK.code() != response.getCode()) {
            Shift.fatal(StatusCode.FETCH_INFO_FAIL);
        }
        return directoryService.findUserDirNameList(response.getUid(), did);
    }

    @RequireRemoteAuth
    @GetMapping(value = {"/dirList/{did}"})
    public Response userDirList(@PathVariable(required = false) String did, RemoteAuthResponse response) {
        if (StatusCode.OK.code() != response.getCode()) {
            Shift.fatal(StatusCode.FETCH_INFO_FAIL);
        }
        Set<BaseFile> set = new TreeSet<>(directoryService.findUserDirList(response.getUid(), did));
        GetUserDirListResponse result = new GetUserDirListResponse();
        result.setFileSet(set);
        return result;
    }

    @RequireRemoteAuth
    @PostMapping("/newDir")
    public Response createNewDir(@RequestBody NewDirRequest request, RemoteAuthResponse authResponse) {
        Directory directory = directoryService.createNewDir(request, authResponse.getUid());
        return new NewDirResponse(directory);
    }

    @RequireRemoteAuth
    @PostMapping("/delete")
    public Response deleteDirAndNote(@RequestBody DeleteRequest request, RemoteAuthResponse authResponse) {
        Integer changeNum = directoryService.batchDeleteDirAndNote(request.getDirs(), request.getNotes(), authResponse.getUid());
        return new DeleteResponse(changeNum);
    }

    @RequireRemoteAuth
    @PatchMapping("/move")
    public Response moveDirAndNote(@RequestBody MoveRequest request, RemoteAuthResponse authResponse) {
        Integer changeNum = directoryService.moveDirAndNote(request.getDirs(), request.getNotes(), request.getDest(), authResponse.getUid());
        return new MoveResponse(changeNum);
    }

    @RequireRemoteAuth
    @PatchMapping("/copy")
    public Response copyDirAndNote(@RequestBody CopyRequest request, RemoteAuthResponse authResponse) {
        Integer changeNum = directoryService.copyDirAndNote(request.getDirs(), request.getNotes(), request.getDest(), authResponse.getUid());
        return new CopyResponse(changeNum);
    }

    @RequireRemoteAuth
    @PostMapping("/toRubbish")
    public Response deleteToRubbish(@RequestBody ToRubbishRequest request, RemoteAuthResponse authResponse) {
        Integer changeNum = directoryService.moveToRubbish(request.getDirs(), request.getNotes(), authResponse.getUid());
        return new ToRubbishResponse(changeNum);
    }

    @RequireRemoteAuth
    @PatchMapping("/renameDir")
    public Response renameDir(@RequestBody RenameDirRequest request, RemoteAuthResponse authResponse) {
        Integer changeNum = directoryService.rename(request.getDid(), request.getNewName(), authResponse.getUid());
        if (changeNum <= 0) {
            Shift.fatal(StatusCode.RENAME_ERROR);
        }
        return new RenameDirResponse(changeNum);
    }

}
