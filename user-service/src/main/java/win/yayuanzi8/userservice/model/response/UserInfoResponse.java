package win.yayuanzi8.userservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import win.yayuanzi8.common.response.RestfulResponse;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class UserInfoResponse extends RestfulResponse {

    private Integer uid;
    private String username;
    private Date registerTime;
    private String portrait;
    private String email;

    public UserInfoResponse(Integer uid, String username, String email, String portrait, Date registerTime) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.portrait = portrait;
        this.registerTime = registerTime;
    }

    public Integer getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getEmail() {
        return email;
    }
}
