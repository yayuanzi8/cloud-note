package win.yayuanzi8.noteservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import win.yayuanzi8.noteservice.domain.Rubbish;

import java.util.List;

public interface RubbishRepository extends MongoRepository<Rubbish, String>, RubbishOperations {
    List<Rubbish> findAllByUidAndRoot(Integer uid, boolean root);

    Rubbish findByUidAndRid(Integer uid, String rid);
}
