package win.yayuanzi8.thirdpartyservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import win.yayuanzi8.common.response.Response;
import win.yayuanzi8.common.util.Shift;
import win.yayuanzi8.thirdpartyservice.controller.model.QiniuResponse;
import win.yayuanzi8.thirdpartyservice.service.QiniuService;
import win.yayuanzi8.thirdpartyservice.util.StatusCode;

import java.io.IOException;

@RestController
public class QiniuObjStorageController {

    private final QiniuService qiniuService;

    @Autowired
    public QiniuObjStorageController(QiniuService qiniuService) {
        this.qiniuService = qiniuService;
    }

    @PostMapping("/upload")
    public Response upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String url = qiniuService.upload(multipartFile.getBytes());
            return new QiniuResponse(url);
        } catch (IOException e) {
            Shift.fatal(StatusCode.IO_ERR);
            return null;
        }
    }
}
