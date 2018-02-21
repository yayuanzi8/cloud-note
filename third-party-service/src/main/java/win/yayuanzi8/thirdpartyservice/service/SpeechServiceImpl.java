package win.yayuanzi8.thirdpartyservice.service;

import com.baidu.aip.speech.AipSpeech;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SpeechServiceImpl implements SpeechService {

    private final AipSpeech aipSpeech;

    @Autowired
    public SpeechServiceImpl(AipSpeech aipSpeech) {
        this.aipSpeech = aipSpeech;
    }

    @Override
    public String recognizeSpeech(byte[] speechData) {
        JSONObject response = aipSpeech.asr(speechData,"pcm",1600,new HashMap<>());
        JSONArray jsonArray = response.getJSONArray("words_result");
        StringBuilder sb = new StringBuilder(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            sb.append(((JSONObject)jsonArray.get(i)).get("words"));
        }
        return sb.toString();
    }
}
