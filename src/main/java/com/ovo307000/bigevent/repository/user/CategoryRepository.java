package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.dto.CategoryDTO;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userCategoryRepository")
public interface CategoryRepository extends JpaRepository<CategoryDTO, Integer>
{
    List<CategoryDTO> findCategoryDTOByCreateUser(UserDTO userDTO);

    CategoryDTO findCategoryDTOById(Long id);
}
