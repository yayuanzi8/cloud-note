package win.yayuanzi8.noteservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import win.yayuanzi8.noteservice.domain.Directory;

import java.util.List;
import java.util.Set;

public interface DirectoryRepository extends MongoRepository<Directory, String>, DirectoryOperations {

    Directory findByUidAndDid(Integer uid, String did);

    List<Directory> findAllByUidAndDidIn(Integer uid, Set<String> dids);

}
