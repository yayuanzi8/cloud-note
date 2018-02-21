package win.yayuanzi8.thirdpartyservice.service;

public interface OCRService {
    String generalRecognition(String url);
    String generalRecognition(byte[] imageBytes);
}
