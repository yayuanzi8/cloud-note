package win.yayuanzi8.noteservice.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import win.yayuanzi8.noteservice.domain.Note;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String>,NoteOperations {

    List<Note> findAllByUidAndParent(Integer uid, String parent);

    Note findByUidAndNid(Integer uid,String nid);

    List<Note> findAllByUidAndNidIn(Integer uid,List<String> nidList);

    Integer removeAllByUidAndNidIn(Integer uid, List<String> noteIdsDeleted);

    List<Note> findAllByUidAndNidIn(Integer uid, List<String> nidList, Sort sort);
}
