package com.ovo307000.bigevent.repository.user;

import com.ovo307000.bigevent.entity.dto.ArticleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userArticleRepository")
public interface ArticleRepository extends JpaRepository<ArticleDTO, Integer>
{
}
