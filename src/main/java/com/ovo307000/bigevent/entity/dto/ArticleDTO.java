package com.ovo307000.bigevent.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "article")
public class ArticleDTO
{
    @Id
    @NotNull(groups = Update.class, message = "article id can not be null")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @NotEmpty(groups = {Update.class, Add.class}, message = "article title cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "article title can only contain letters, numbers and underscores")
    private String title;

    @Column(nullable = false, length = 10000)
    @NotEmpty(groups = {Update.class, Add.class}, message = "article content cannot be empty")
    private String content;

    @URL
    @Column(nullable = false, length = 128)
    @NotEmpty(groups = {Update.class, Add.class}, message = "article cover image cannot be empty")
    private String coverImg;

    @Column(length = 3)
    @NotEmpty(groups = {Update.class, Add.class}, message = "article state cannot be empty")
    @Pattern(regexp = "^(草稿|发布)$", message = "article state can only be '草稿' or '发布'")
    private String state = "草稿";

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotEmpty(groups = {Update.class, Add.class}, message = "article category cannot be empty")
    private CategoryDTO category;

    @ManyToOne
    @JoinColumn(name = "create_user", nullable = false)
    private UserDTO createUser;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public ArticleDTO(String title,
                      String content,
                      String coverImg,
                      String state,
                      CategoryDTO category,
                      UserDTO createUser,
                      LocalDateTime createTime,
                      LocalDateTime updateTime)
    {
        this.title      = title;
        this.content    = content;
        this.coverImg   = coverImg;
        this.state      = state;
        this.category   = category;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ArticleDTO()
    {
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id,
                            this.title,
                            this.content,
                            this.coverImg,
                            this.state,
                            this.category,
                            this.createUser,
                            this.createTime,
                            this.updateTime);
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
        ArticleDTO article = (ArticleDTO) o;
        return Objects.equals(this.id, article.id) &&
               Objects.equals(this.title, article.title) &&
               Objects.equals(this.content, article.content) &&
               Objects.equals(this.coverImg, article.coverImg) &&
               Objects.equals(this.state, article.state) &&
               Objects.equals(this.category, article.category) &&
               Objects.equals(this.createUser, article.createUser) &&
               Objects.equals(this.createTime, article.createTime) &&
               Objects.equals(this.updateTime, article.updateTime);
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCoverImg()
    {
        return this.coverImg;
    }

    public void setCoverImg(String coverImg)
    {
        this.coverImg = coverImg;
    }

    public String getState()
    {
        return this.state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public CategoryDTO getCategory()
    {
        return this.category;
    }

    public void setCategory(CategoryDTO category)
    {
        this.category = category;
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

    public interface Add
    {

    }

    public interface Update
    {

    }
}
