package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.dto.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userCategoryRepository")
public interface CategoryRepository extends JpaRepository<CategoryDTO, Integer>
{

}
