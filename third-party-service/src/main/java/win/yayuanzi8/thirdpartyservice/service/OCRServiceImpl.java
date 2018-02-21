package win.yayuanzi8.thirdpartyservice.service;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OCRServiceImpl implements OCRService {

    private final AipOcr aipOcr;

    @Autowired
    public OCRServiceImpl(AipOcr aipOcr) {
        this.aipOcr = aipOcr;
    }

    @Override
    public String generalRecognition(String url) {
        JSONObject response = aipOcr.basicGeneralUrl(url, new HashMap<>());
        JSONArray jsonArray = response.getJSONArray("words_result");
        StringBuilder sb = new StringBuilder(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            sb.append(((JSONObject)jsonArray.get(i)).get("words"));
        }
        return sb.toString();
    }

    @Override
    public String generalRecognition(byte[] imageBytes) {
        JSONObject response = aipOcr.basicGeneral(imageBytes, new HashMap<>());
        JSONArray jsonArray = response.getJSONArray("words_result");
        StringBuilder sb = new StringBuilder(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            sb.append(((JSONObject)jsonArray.get(i)).get("words"));
        }
        return sb.toString();
    }
}
