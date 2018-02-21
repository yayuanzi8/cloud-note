package win.yayuanzi8.noteservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.noteservice.aspect.RequireRemoteAuth;
import win.yayuanzi8.noteservice.domain.BaseFile;
import win.yayuanzi8.noteservice.domain.Rubbish;
import win.yayuanzi8.noteservice.model.request.RecoverRequest;
import win.yayuanzi8.noteservice.model.response.GetAllMyRubbishResponse;
import win.yayuanzi8.noteservice.model.response.RecoverResponse;
import win.yayuanzi8.noteservice.model.response.RemoteAuthResponse;
import win.yayuanzi8.noteservice.service.RubbishService;

import java.util.List;

@RestController
public class RubbishController {

    private final RubbishService rubbishService;

    @Autowired
    public RubbishController(RubbishService rubbishService) {
        this.rubbishService = rubbishService;
    }


    @RequireRemoteAuth
    @GetMapping("/myRubbish")
    public Response allMyRubbish(RemoteAuthResponse authResponse) {
        List<Rubbish> rubbishes = rubbishService.getAllMyRubbish(authResponse.getUid());
        return new GetAllMyRubbishResponse(rubbishes);
    }

    @RequireRemoteAuth
    @PostMapping("/recover")
    public Response recoverDirAndNote(@RequestBody RecoverRequest request , RemoteAuthResponse authResponse) {
        List<BaseFile> insertedFiles = rubbishService.recover(request.getRid() ,authResponse.getUid());
        return new RecoverResponse(insertedFiles.size());
    }
}
