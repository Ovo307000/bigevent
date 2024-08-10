package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userCategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Integer>
{
}
