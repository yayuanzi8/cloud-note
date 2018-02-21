package win.yayuanzi8.thirdpartyservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.thirdpartyservice.controller.model.ReconizeResponse;
import win.yayuanzi8.thirdpartyservice.service.SpeechService;
import win.yayuanzi8.thirdpartyservice.util.StatusCode;

import java.io.IOException;

@RestController
public class SpeechController {

    private final SpeechService speechService;

    @Autowired
    public SpeechController(SpeechService speechService) {
        this.speechService = speechService;
    }

    @PostMapping("/speech")
    public Response speech(@RequestParam("audioFile") MultipartFile speechFile) {
        try {
            return new ReconizeResponse(speechService.recognizeSpeech(speechFile.getBytes()));
        } catch (IOException e) {
            Shift.fatal(StatusCode.IO_ERR);
            return null;
        }
    }
}
