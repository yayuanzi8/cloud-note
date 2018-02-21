package win.yayuanzi8.noteservice.service;

import win.yayuanzi8.noteservice.domain.BaseFile;
import win.yayuanzi8.noteservice.domain.Rubbish;

import java.util.List;

public interface RubbishService {
    List<Rubbish> getAllMyRubbish(Integer uid);

    List<BaseFile> recover(String rid, Integer uid);
}
