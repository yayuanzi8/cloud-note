package win.yayuanzi8.thirdpartyservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.thirdpartyservice.controller.model.ReconizeResponse;
import win.yayuanzi8.thirdpartyservice.service.OCRService;
import win.yayuanzi8.thirdpartyservice.util.StatusCode;

import java.io.IOException;

@RestController
public class OCRController {

    private final OCRService ocrService;

    @Autowired
    public OCRController(OCRService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/ocr")
    public Response ocr(@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            return new ReconizeResponse(ocrService.generalRecognition(imageFile.getBytes()));
        } catch (IOException e) {
            Shift.fatal(StatusCode.IO_ERR);
            return null;
        }
    }
}
