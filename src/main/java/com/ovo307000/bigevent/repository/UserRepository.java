package com.ovo307000.bigevent.repository;

import com.ovo307000.bigevent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer>
{
    List<User> findUsersByNickname(@Param("nickname") String nickname);

    List<User> findUsersByNicknameLikeIgnoreCase(@Param("nickname") String nickname);

    User findUsersByUsername(@Param("username") String username);

    List<User> findUsersByUsernameLikeIgnoreCase(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("""
           update User user
           set user.username = :newUsername,
               user.nickname = :newNickname,
               user.password = :newPassword,
               user.email = :newEmail,
               user.userPic = :newPicture,
               user.updateTime = :updateTime
           where user.username = :username
           """)
    int updateUserByUsername(@Param("username") String username,
                             @Param("newUsername") String newUsername,
                             @Param("newNickname") String newNickname,
                             @Param("newPassword") String newPassword,
                             @Param("newEmail") String newEmail,
                             @Param("newPicture") String newPicture,
                             @Param("updateTime") LocalDateTime updateTime);


}

