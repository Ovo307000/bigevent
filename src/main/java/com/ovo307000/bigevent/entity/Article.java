package com.ovo307000.bigevent.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "article")
public class Article
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false, length = 128)
    private String coverImg;

    @Column(length = 3)
    private String state = "草稿";

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "create_user", nullable = false)
    private User createUser;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    public Article(String title,
                   String content,
                   String coverImg,
                   String state,
                   Category category,
                   User createUser,
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

    public Article()
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
        Article article = (Article) o;
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

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
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

    public Category getCategory()
    {
        return this.category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
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
}
