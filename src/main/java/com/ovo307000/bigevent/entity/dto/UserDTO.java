package com.ovo307000.bigevent.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class UserDTO
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @JsonIgnore
    @Column(length = 256)
    private String password;

    @Column(length = 10)
    private String nickname = "";

    @Column(length = 128)
    private String email = "";

    @Column(length = 128)
    private String userPic = "";

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;


    // Mapping to other entities
    @OneToMany(mappedBy = "createUser")
    private Set<CategoryDTO> categories;

    @OneToMany(mappedBy = "createUser")
    private Set<ArticleDTO> articles;

    public UserDTO(String username,
                   String password,
                   String nickname,
                   String email,
                   String userPic,
                   LocalDateTime createTime,
                   LocalDateTime updateTime,
                   Set<CategoryDTO> categories,
                   Set<ArticleDTO> articles)
    {
        this.username   = username;
        this.password   = password;
        this.nickname   = nickname;
        this.email      = email;
        this.userPic    = userPic;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.categories = categories;
        this.articles   = articles;
    }

    public UserDTO()
    {
    }

    public UserDTO(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public UserDTO(String username, String password, String nickname, String email, String userPic)
    {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email    = email;
        this.userPic  = userPic;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id,
                            this.username,
                            this.password,
                            this.nickname,
                            this.email,
                            this.userPic,
                            this.createTime,
                            this.updateTime,
                            this.categories,
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
        UserDTO user = (UserDTO) o;
        return Objects.equals(this.id, user.id) &&
               Objects.equals(this.username, user.username) &&
               Objects.equals(this.password, user.password) &&
               Objects.equals(this.nickname, user.nickname) &&
               Objects.equals(this.email, user.email) &&
               Objects.equals(this.userPic, user.userPic) &&
               Objects.equals(this.createTime, user.createTime) &&
               Objects.equals(this.updateTime, user.updateTime) &&
               Objects.equals(this.categories, user.categories) &&
               Objects.equals(this.articles, user.articles);
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNickname()
    {
        return this.nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUserPic()
    {
        return this.userPic;
    }

    public void setUserPic(String userPic)
    {
        this.userPic = userPic;
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

    public Set<CategoryDTO> getCategories()
    {
        return this.categories;
    }

    public void setCategories(Set<CategoryDTO> categories)
    {
        this.categories = categories;
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
