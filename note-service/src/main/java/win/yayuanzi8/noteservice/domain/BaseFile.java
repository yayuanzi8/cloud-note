package win.yayuanzi8.noteservice.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yayua
 */
public abstract class BaseFile implements Serializable, Comparable<BaseFile> {

    //创建时间
    private Date createTime;

    //所属用户
    private Integer uid;

    //类型：note或directory
    private String type;

    //物化路径：/1/2/3
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseFile file = (BaseFile) o;

        if (createTime != null ? !createTime.equals(file.createTime) : file.createTime != null) return false;
        if (uid != null ? !uid.equals(file.uid) : file.uid != null) return false;
        if (type != null ? !type.equals(file.type) : file.type != null) return false;
        return path != null ? path.equals(file.path) : file.path == null;
    }

    @Override
    public int hashCode() {
        int result = createTime != null ? createTime.hashCode() : 0;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(BaseFile file) {
        int typeResult = type.compareTo(file.type);
        if (typeResult == 0) {
            return this.createTime.compareTo(file.createTime);
        }
        return typeResult;
    }
}
