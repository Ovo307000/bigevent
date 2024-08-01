package com.ovo307000.bigevent.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 32)
    private String categoryName;

    @Column(nullable = false, length = 32)
    private String categoryAlias;

    @ManyToOne
    @JoinColumn(name = "create_user", nullable = false)
    private User createUser;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    // Constructors, getters, setters, etc.

    // Mapping to other entities
    @OneToMany(mappedBy = "category")
    private Set<Article> articles;

    public Category()
    {
    }

    public Category(String categoryName,
                    String categoryAlias,
                    User createUser,
                    LocalDateTime createTime,
                    LocalDateTime updateTime,
                    Set<Article> articles)
    {


        this.categoryName  = categoryName;
        this.categoryAlias = categoryAlias;
        this.createUser    = createUser;
        this.createTime    = createTime;
        this.updateTime    = updateTime;
        this.articles      = articles;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id,
                            this.categoryName,
                            this.categoryAlias,
                            this.createUser,
                            this.createTime,
                            this.updateTime,
                            this.articles);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        Category category = (Category) o;
        return Objects.equals(this.id, category.id) &&
               Objects.equals(this.categoryName, category.categoryName) &&
               Objects.equals(this.categoryAlias, category.categoryAlias) &&
               Objects.equals(this.createUser, category.createUser) &&
               Objects.equals(this.createTime, category.createTime) &&
               Objects.equals(this.updateTime, category.updateTime) &&
               Objects.equals(this.articles, category.articles);
    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryAlias()
    {
        return this.categoryAlias;
    }

    public void setCategoryAlias(String categoryAlias)
    {
        this.categoryAlias = categoryAlias;
    }

    public User getCreateUser()
    {
        return this.createUser;
    }

    public void setCreateUser(User createUser)
    {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(LocalDateTime createTime)
    {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime)
    {
        this.updateTime = updateTime;
    }

    public Set<Article> getArticles()
    {
        return this.articles;
    }

    public void setArticles(Set<Article> articles)
    {
        this.articles = articles;
    }
}
