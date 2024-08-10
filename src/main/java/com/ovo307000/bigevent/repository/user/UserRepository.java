package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("userUserRepository")
public interface UserRepository extends JpaRepository<User, Integer>
{
    List<User> findUsersByNickname(@Param("nickname") String nickname);

    List<User> findUsersByNicknameLikeIgnoreCase(@Param("nickname") String nickname);

    User findUsersByUsername(@Param("username") String username);

    List<User> findUsersByUsernameLikeIgnoreCase(@Param("username") String username);

    List<User> findUsersByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("""
           update User as user
           set user.nickname   = :#{#user.nickname},
               user.password   = :#{#user.password},
               user.email      = :#{#user.email},
               user.updateTime = :#{#user.updateTime}
           where user.id = :#{#id}
           """)
    int updateUserById(@Param("id") Long id, @Param("user") User user);
}

