package win.yayuanzi8.noteservice.repository;

import win.yayuanzi8.noteservice.domain.Rubbish;

import java.util.List;

public interface RubbishOperations {
    List<Rubbish> findAllByUidAndPath(Integer uid, String path);
}
