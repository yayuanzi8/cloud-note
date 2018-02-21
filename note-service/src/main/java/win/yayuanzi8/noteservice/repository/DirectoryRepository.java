package win.yayuanzi8.noteservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import win.yayuanzi8.noteservice.domain.Directory;

import java.util.List;
import java.util.Set;

public interface DirectoryRepository extends MongoRepository<Directory,String>,DirectoryOperations {
    List<Directory> findByUid(Integer uid);

    Directory findByUidAndDid(Integer uid, String did);

    List<Directory> findAllByUidAndParent(Integer uid,String parent);

    List<Directory> findAllByUidAndDidIn(Integer uid, Set<String> dids);

    Integer removeAllByUidAndDidIn(Integer uid, List<String> didList);
}
