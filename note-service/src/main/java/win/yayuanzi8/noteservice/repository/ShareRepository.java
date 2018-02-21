package win.yayuanzi8.noteservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import win.yayuanzi8.noteservice.domain.ShareNote;

import java.util.List;

public interface ShareRepository extends MongoRepository<ShareNote, String> {

    List<ShareNote> findAllByUidOrderByShareDateDesc(Integer uid);

    Integer deleteByUidAndSid(Integer uid, String sid);

    ShareNote findByNid(String nid);

}
