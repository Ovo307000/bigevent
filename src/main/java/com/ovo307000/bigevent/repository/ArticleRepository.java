package com.ovo307000.bigevent.repository;

import com.ovo307000.bigevent.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("articleRepository")
public interface ArticleRepository extends JpaRepository<Article, Integer>
{
}
