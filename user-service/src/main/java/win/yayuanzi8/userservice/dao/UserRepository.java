package win.yayuanzi8.userservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import win.yayuanzi8.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(@Param("username") String username);

    User findByUsernameAndPassword(String username, String password);

    int countByEmail(String email);

    User findByUid(Integer uid);

    @Modifying
    @Query(value = "update User u set u.email=:email,u.password=:password,u.portrait=:portrait " +
            "where u.uid=:uid")
    void update(@Param("email")String email,
                @Param("password")String password,
                @Param("portrait")String portrait,
                @Param("uid")Integer uid);

}
