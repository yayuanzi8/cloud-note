package win.yayuanzi8.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final UserDetailsService userDetailsService;
//    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /*@Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService*//*, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter*//*) {
        this.userDetailsService = userDetailsService;
//        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }*/

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }*/

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                /*.and()
                .authorizeRequests().anyRequest().permitAll();
                .antMatchers(HttpMethod.POST, "/auth/register", "/auth")
                .permitAll()
                .antMatchers("/auth/logout").authenticated()
                .antMatchers("/loginUserInfo","/refresh").authenticated();*/
        httpSecurity.headers().cacheControl();
        /*httpSecurity
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
