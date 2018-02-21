package win.yayuanzi8.userservice.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import win.yayuanzi8.userservice.domain.User;

import java.util.Collection;
import java.util.Date;

public final class JwtUser implements UserDetails {
    private final Integer uid;
    private final String username;
    private final String password;
    private final String email;
    private final Date registerTime;
    private final String portrait;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(
            Integer uid,
            String username,
            String password,
            String email,
            Date registerTime, String portrait, Collection<? extends GrantedAuthority> authorities) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registerTime = registerTime;
        this.portrait = portrait;
        this.authorities = authorities;
    }
    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getUid() {
        return uid;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    // 账户是否未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 账户是否未锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 密码是否未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 账户是否激活
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public String getPortrait() {
        return portrait;
    }

    public User convertToUser() {
        User user = new User();
        user.setUid(uid);
        user.setUsername(username);
        user.setEmail(email);
        user.setPortrait(portrait);
        user.setRegisterTime(registerTime);
        return user;
    }
}
