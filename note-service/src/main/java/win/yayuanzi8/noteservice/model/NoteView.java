package win.yayuanzi8.noteservice.model;

import java.io.Serializable;
import java.util.Date;

public class NoteView implements Serializable,Comparable<NoteView> {
    private String nid;
    private String title;
    private Date shareDate;
    private String sid;
    private String content;
    private String username;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public int compareTo(NoteView another) {
        int result = another.shareDate.compareTo(shareDate);
        if (result == 0){
            result = another.title.compareTo(this.title);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteView noteView = (NoteView) o;

        if (nid != null ? !nid.equals(noteView.nid) : noteView.nid != null) return false;
        if (title != null ? !title.equals(noteView.title) : noteView.title != null) return false;
        return shareDate != null ? shareDate.equals(noteView.shareDate) : noteView.shareDate == null;
    }

    @Override
    public int hashCode() {
        int result = nid != null ? nid.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (shareDate != null ? shareDate.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("/测试1/emmmm/gg".matches("^/测试1/[^/<>?*,|]+$"));
        System.out.println("/测试1/dsjghfgsdjf解决".matches("^/测试1/[^/<>?*,|]+$"));
    }
}
