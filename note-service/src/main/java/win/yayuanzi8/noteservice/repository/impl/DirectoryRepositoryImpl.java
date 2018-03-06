package win.yayuanzi8.noteservice.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import win.yayuanzi8.noteservice.domain.Directory;
import win.yayuanzi8.noteservice.repository.BatchUpdateOptions;
import win.yayuanzi8.noteservice.repository.DirectoryOperations;
import win.yayuanzi8.noteservice.util.MongoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DirectoryRepositoryImpl implements DirectoryOperations {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Integer batchDeleteByPath(Set<String> regexSet, Integer uid) {
        Criteria criteria = MongoUtil.fillCriteria(uid, regexSet);
        return mongoTemplate.remove(Query.query(criteria), Directory.class).getN();
    }

    @Override
    public List<Directory> batchQuery(Integer uid, Set<String> regexSet) {
        Query query = Query.query(MongoUtil.fillCriteria(uid, regexSet));
        return mongoTemplate.find(query, Directory.class, "directory");
    }

    /**
     * 批量更新
     *
     * @param uid              用户uid
     * @param dirWillBeUpdated 更新集合
     * @return 更改的记录数目
     */
    @Override
    public Integer batchUpdate(Integer uid, List<Directory> dirWillBeUpdated) {
        if (dirWillBeUpdated.size() > 0) {
            DBObject command = new BasicDBObject();
            command.put("update", "directory");
            List<BasicDBObject> updateList = new ArrayList<>();
            for (Directory directory : dirWillBeUpdated) {
                Update update1 = new Update();
                update1.set("path", directory.getPath());
                update1.set("dirName", directory.getDirName());
                BatchUpdateOptions option = new BatchUpdateOptions(Query.query(Criteria.where("uid").is(uid).and("_id").is(new ObjectId(directory.getDid()))),
                        update1, false, true);
                BasicDBObject update = new BasicDBObject();
                update.put("q", option.getQuery().getQueryObject());
                update.put("u", option.getUpdate().getUpdateObject());
                update.put("upsert", option.isUpsert());
                update.put("multi", option.isMulti());
                updateList.add(update);
            }
            command.put("updates", updateList);
            command.put("ordered", false);
            CommandResult commandResult = mongoTemplate.executeCommand(command);
            return Integer.parseInt(commandResult.get("n").toString());
        }
        return 0;
    }
}
