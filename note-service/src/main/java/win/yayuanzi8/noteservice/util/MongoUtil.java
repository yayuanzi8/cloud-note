package win.yayuanzi8.noteservice.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MongoUtil {
    public static Criteria fillCriteria(Integer uid, Set<String> regexSet) {
        Criteria criteria = Criteria.where("uid").is(uid);
        List<Criteria> regexList = new ArrayList<>();
        regexSet.forEach(regex -> {
            regexList.add(Criteria.where("path").regex(regex));
        });
        Criteria[] criteriaArray = new Criteria[regexList.size()];
        criteriaArray = regexList.toArray(criteriaArray);
        criteria = criteria.orOperator(criteriaArray);
        return criteria;
    }
}
