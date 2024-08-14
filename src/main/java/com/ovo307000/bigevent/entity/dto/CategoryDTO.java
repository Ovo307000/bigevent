package com.ovo307000.bigevent.entity.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "category")
public class CategoryDTO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 32)
    private String categoryName;

    @NotEmpty
    @Column(nullable = false, length = 32)
    private String categoryAlias;

    @ManyToOne
    @JoinColumn(name = "create_user", nullable = false)
    private UserDTO createUser;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    // Constructors, getters, setters, etc.

    // Mapping to other entities
    @OneToMany(mappedBy = "category")
    private Set<ArticleDTO> articles;

    public CategoryDTO()
    {
    }

    public CategoryDTO(String categoryName,
                       String categoryAlias,
                       UserDTO createUser,
                       LocalDateTime createTime,
                       LocalDateTime updateTime,
                       Set<ArticleDTO> articles)
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
        CategoryDTO category = (CategoryDTO) o;
        return Objects.equals(this.id, category.id) &&
               Objects.equals(this.categoryName, category.categoryName) &&
               Objects.equals(this.categoryAlias, category.categoryAlias) &&
               Objects.equals(this.createUser, category.createUser) &&
               Objects.equals(this.createTime, category.createTime) &&
               Objects.equals(this.updateTime, category.updateTime) &&
               Objects.equals(this.articles, category.articles);
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
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

    public UserDTO getCreateUser()
    {
        return this.createUser;
    }

    public void setCreateUser(UserDTO createUser)
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

    public Set<ArticleDTO> getArticles()
    {
        return this.articles;
    }

    public void setArticles(Set<ArticleDTO> articles)
    {
        this.articles = articles;
    }
}
