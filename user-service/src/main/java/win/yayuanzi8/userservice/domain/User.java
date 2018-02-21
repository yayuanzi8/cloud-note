package win.yayuanzi8.userservice.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//IDENTITY:数据库表自增。默认是AUTO
    private Integer uid;
    @Column(nullable = false, unique = true, length = 20)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;
    @Column(nullable = false, name = "register_time")
    private Date registerTime;
    @Column(nullable = false, name = "portrait")
    private String portrait;
    @Column(nullable = false, name = "email")
    private String email;
    @Transient
    private static final long serialVersionUID = 10086L;

    public User() {
    }

    public User(String username, String password, String email, String portrait) {
        this.username = username;
        this.password = password;
        this.portrait = portrait;
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registerTime=" + registerTime +
                '}';
    }
}
