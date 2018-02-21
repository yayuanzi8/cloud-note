package win.yayuanzi8.noteservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.noteservice.aspect.RequireRemoteAuth;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.model.request.NewNoteRequest;
import win.yayuanzi8.noteservice.model.request.RenameNoteRequest;
import win.yayuanzi8.noteservice.model.request.UpdateNoteRequest;
import win.yayuanzi8.noteservice.model.response.*;
import win.yayuanzi8.noteservice.repository.NoteRepository;
import win.yayuanzi8.noteservice.service.NoteService;
import win.yayuanzi8.noteservice.util.StatusCode;

@RestController
public class NoteController {
    private final NoteRepository noteRepository;
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteRepository noteRepository, NoteService noteService) {
        this.noteRepository = noteRepository;
        this.noteService = noteService;
    }

    @RequireRemoteAuth
    @GetMapping("/usernote/{nid}")
    public Response getNote(@PathVariable("nid")String nid, RemoteAuthResponse authResponse){
        Note note = noteRepository.findByUidAndNid(authResponse.getUid(),nid);
        return new GetNoteResponse(note);
    }

    @RequireRemoteAuth
    @PostMapping("/newNote")
    public Response createNewNote(@RequestBody NewNoteRequest request, RemoteAuthResponse authResponse){
        Note note = noteService.createNewNote(request.getTitle(),request.getParent(),authResponse.getUid());
        return new NewNoteResponse(note);
    }

    @RequireRemoteAuth
    @PostMapping("/update")
    public Response updateNote(@RequestBody UpdateNoteRequest request, RemoteAuthResponse authResponse){
        Note note = noteService.updateNote(request.getNid(),request.getTitle(),request.getContent(),authResponse.getUid());
        return new UpdateNoteResponse(note);
    }

    @RequireRemoteAuth
    @PatchMapping("/renameNote")
    public Response renameNote(@RequestBody RenameNoteRequest request, RemoteAuthResponse authResponse){
        Integer changeNum = noteService.rename(request.getNid(), request.getNewName(), authResponse.getUid());
        if (changeNum <=0 ){
            Shift.fatal(StatusCode.RENAME_ERROR);
        }
        return new RenameNoteResponse(changeNum);
    }

}
