package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.dto.ArticleDTO;
import com.ovo307000.bigevent.entity.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userArticleRepository")
public interface ArticleRepository extends JpaRepository<ArticleDTO, Long>
{
    Page<ArticleDTO> findAllByCreateUserAndState(UserDTO user, String status, PageRequest pageRequest);

    Page<ArticleDTO> findAllByCreateUserAndCategoryIdAndState(UserDTO user,
                                                              Long categoryId,
                                                              String status,
                                                              PageRequest pageRequest);
}
