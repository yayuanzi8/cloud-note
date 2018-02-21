package win.yayuanzi8.noteservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "rubbish")
public class Rubbish {
    //id，不可更改
    @Id
    private String rid;
    private BaseFile file;
    private Integer uid;
    private boolean root = false;//默认非root

    public Rubbish() {
    }


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public BaseFile getFile() {
        return file;
    }

    public void setFile(BaseFile file) {
        this.file = file;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
