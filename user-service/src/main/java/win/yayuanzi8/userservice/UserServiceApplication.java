package win.yayuanzi8.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        /*Map<String, Object> claims = new HashMap<>();
        claims.put("username", "亚原子8");
        claims.put("date", new Date());
        String token = JWTUtil.generateToken(claims);
        System.out.println(token);
        Claims claims1 = JWTUtil.getClaimsFromToken(token);
        Set<Map.Entry<String, Object>> valueCollection = claims1.entrySet();
        for (Map.Entry<String, Object> entry : valueCollection) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
            if (entry.getKey().equalsIgnoreCase("date")){
                System.out.println(new Date((Long) entry.getValue()));
            }
        }*/
    }
}
