package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userUserRepository")
public interface UserRepository extends JpaRepository<UserDTO, Integer>
{
    List<UserDTO> findUsersByNickname(@Param("nickname") String nickname);

    List<UserDTO> findUsersByNicknameLikeIgnoreCase(@Param("nickname") String nickname);

    UserDTO findUsersByUsername(@Param("username") String username);

    UserDTO findUsersById(@Param("id") Long id);

    List<UserDTO> findUsersByUsernameLikeIgnoreCase(@Param("username") String username);
}

