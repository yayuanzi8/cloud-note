package win.yayuanzi8.noteservice.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import win.yayuanzi8.noteservice.domain.Rubbish;
import win.yayuanzi8.noteservice.repository.RubbishOperations;

import java.util.List;

public class RubbishRepositoryImpl implements RubbishOperations {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Rubbish> findAllByUidAndPath(Integer uid, String path) {
        Criteria criteria = Criteria.where("uid").is(uid).and("file.path").regex("^" + path);
        Query query = Query.query(criteria);
        return mongoTemplate.find(query,Rubbish.class);
    }
}
