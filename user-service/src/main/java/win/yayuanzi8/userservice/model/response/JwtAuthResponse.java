package win.yayuanzi8.userservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.userdetails.UserDetails;
import win.yayuanzi8.common.response.RestfulResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class JwtAuthResponse extends RestfulResponse {

    private final String token;
    private long expDuration;
    private UserDetails jwtUser;

    public JwtAuthResponse(String token, long expDuration, UserDetails jwtUser) {
        this.token = token;
        this.expDuration = expDuration;
        this.jwtUser = jwtUser;
    }

    public JwtAuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public UserDetails getJwtUser() {
        return jwtUser;
    }

    public long getExpDuration() {
        return expDuration;
    }
}
