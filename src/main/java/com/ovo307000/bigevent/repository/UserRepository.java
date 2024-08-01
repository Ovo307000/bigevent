package com.ovo307000.bigevent.repository;

import com.ovo307000.bigevent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer>
{
    User findUsersByNickname(@Param("nickname") String nickname);

    User findUsersByUsername(@Param("username") String username);
}

