package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.dto.CategoryDTO;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("userCategoryRepository")
public interface CategoryRepository extends JpaRepository<CategoryDTO, Long>
{
    List<CategoryDTO> findCategoryDTOByCreateUser(UserDTO userDTO);

    CategoryDTO findCategoryDTOById(Long id);

    @Modifying
    @Transactional
    @Query("""
           update CategoryDTO as category
           set category.createUser    = :#{#categoryDTO.createUser},
               category.updateTime    = :#{#categoryDTO.updateTime},
               category.categoryName  = :#{#categoryDTO.categoryName},
               category.categoryAlias = :#{#categoryDTO.categoryAlias}
           where category.id = :#{#categoryDTO.id}
           """)
    Integer updateCategoryDTO(CategoryDTO categoryDTO);
}
