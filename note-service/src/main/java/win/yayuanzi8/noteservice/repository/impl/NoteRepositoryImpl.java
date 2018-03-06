package win.yayuanzi8.noteservice.repository.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import win.yayuanzi8.noteservice.domain.Note;
import win.yayuanzi8.noteservice.repository.BatchUpdateOptions;
import win.yayuanzi8.noteservice.repository.NoteOperations;
import win.yayuanzi8.noteservice.util.MongoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteRepositoryImpl implements NoteOperations {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Note updateNote(Note note) {
        Criteria criteria = Criteria.where("nid").is(note.getNid());
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("title", note.getTitle());
        update.set("content", note.getContent());
        update.set("path", note.getPath());
        mongoTemplate.updateFirst(query,update,Note.class,"note");
        return note;
    }

    @Override
    public int batchDeleteByNid(Integer uid, Set<String> noteIds) {
        Query query = Query.query(Criteria.where("uid").is(uid).and("nid").in(noteIds));
        WriteResult writeResult = mongoTemplate.remove(query, Note.class);
        return writeResult.getN();
    }

    @Override
    public int batchDeleteByPath(Set<String> regexSet, Integer uid) {
        Integer count = 0;
        Criteria criteria = MongoUtil.fillCriteria(uid, regexSet);
        count += mongoTemplate.remove(Query.query(criteria), Note.class).getN();
        return count;
    }

    @Override
    public List<Note> batchQuery(Integer uid, Set<String> regexSet) {
        Criteria criteria = MongoUtil.fillCriteria(uid, regexSet);
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Note.class);
    }

    @Override
    public Integer batchUpdate(Integer uid, List<Note> noteWillBeUpdated) {
        if (noteWillBeUpdated.size() > 0) {
            DBObject command = new BasicDBObject();
            command.put("update", "note");
            List<BasicDBObject> updateList = new ArrayList<>();
            for (Note note : noteWillBeUpdated) {
                Update update1 = new Update();
                update1.set("path", note.getPath());
                update1.set("title", note.getTitle());
                BatchUpdateOptions option = new BatchUpdateOptions(Query.query(Criteria.where("uid").is(uid).and("_id").is(new ObjectId(note.getNid()))),
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
