package win.yayuanzi8.noteservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.noteservice.aspect.RequireRemoteAuth;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.domain.ShareNote;
import win.yayuanzi8.noteservice.model.NoteView;
import win.yayuanzi8.noteservice.model.request.CancelShareRequest;
import win.yayuanzi8.noteservice.model.request.ShareRequest;
import win.yayuanzi8.noteservice.model.response.*;
import win.yayuanzi8.noteservice.service.ShareNoteService;

import java.util.List;
import java.util.Set;

@RestController
public class ShareController {

    private final ShareNoteService shareNoteService;

    @Autowired
    public ShareController(ShareNoteService shareNoteService) {
        this.shareNoteService = shareNoteService;
    }

    @RequireRemoteAuth
    @PostMapping("/share")
    public Response share(@RequestBody ShareRequest request, RemoteAuthResponse authResponse) {
        ShareNote shareNote = shareNoteService.share(request.getNid(),authResponse.getUid());
        return new ShareResponse(shareNote.getSid());
    }

    @RequireRemoteAuth
    @PatchMapping("/cancelShare")
    public Response cancelShare(@RequestBody CancelShareRequest request, RemoteAuthResponse authResponse){
        Integer changeNum = shareNoteService.cancelShare(request.getSid(),authResponse.getUid());
        return new CancelShareResponse(changeNum);
    }

    @RequireRemoteAuth
    @GetMapping("/myShare")
    public Response getMyShare(RemoteAuthResponse authResponse){
        Set<NoteView> noteViewSet = shareNoteService.getMyShare(authResponse.getUid());
        return new ShareListResponse(noteViewSet);
    }

    @GetMapping("/share/{sid}")
    public Response getShareNote(@PathVariable("sid")String sid){
        Note note = shareNoteService.getShare(sid);
        return new GetCommonShareResponse(note);
    }
}
