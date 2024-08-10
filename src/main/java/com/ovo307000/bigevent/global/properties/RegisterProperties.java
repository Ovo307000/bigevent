package com.ovo307000.bigevent.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration("registerProperties")
@ConfigurationProperties(prefix = "bigevent.user.register")
public class RegisterProperties
{
    private Boolean enablePasswordCheck;
    private Integer minPasswordLength;
    private Integer maxPasswordLength;
    private String  passwordRegex;

    private Boolean IsPasswordNeedNumber;
    private Boolean IsPasswordNeedUppercase;
    private Boolean IsPasswordNeedSpecialCharacter;

    private Boolean enableUsernameCheck;
    private Integer minUsernameLength;
    private Integer maxUsernameLength;

    private Boolean enableNicknameCheck;
    private Integer minNicknameLength;
    private Integer maxNicknameLength;

    private Boolean      enablePictureCheck;
    private List<String> pictureFormats;
    private Long         minPictureSizeOfBytes;
    private Long         maxPictureSizeOfBytes;

    private Boolean      enableEmailCheck;
    private List<String> emailFormats;

    private List<String> specialCharacters;

    public Boolean getEnablePasswordCheck()
    {
        return this.enablePasswordCheck;
    }

    public void setEnablePasswordCheck(Boolean enablePasswordCheck)
    {
        this.enablePasswordCheck = enablePasswordCheck;
    }

    public Integer getMinPasswordLength()
    {
        return this.minPasswordLength;
    }

    public void setMinPasswordLength(Integer minPasswordLength)
    {
        this.minPasswordLength = minPasswordLength;
    }

    public Integer getMaxPasswordLength()
    {
        return this.maxPasswordLength;
    }

    public void setMaxPasswordLength(Integer maxPasswordLength)
    {
        this.maxPasswordLength = maxPasswordLength;
    }

    public String getPasswordRegex()
    {
        return this.passwordRegex;
    }

    public void setPasswordRegex(String passwordRegex)
    {
        this.passwordRegex = passwordRegex;
    }

    public Boolean getPasswordNeedNumber()
    {
        return this.IsPasswordNeedNumber;
    }

    public void setPasswordNeedNumber(Boolean passwordNeedNumber)
    {
        this.IsPasswordNeedNumber = passwordNeedNumber;
    }

    public Boolean getPasswordNeedUppercase()
    {
        return this.IsPasswordNeedUppercase;
    }

    public void setPasswordNeedUppercase(Boolean passwordNeedUppercase)
    {
        this.IsPasswordNeedUppercase = passwordNeedUppercase;
    }

    public Boolean getPasswordNeedSpecialCharacter()
    {
        return this.IsPasswordNeedSpecialCharacter;
    }

    public void setPasswordNeedSpecialCharacter(Boolean passwordNeedSpecialCharacter)
    {
        this.IsPasswordNeedSpecialCharacter = passwordNeedSpecialCharacter;
    }

    public Boolean getEnableUsernameCheck()
    {
        return this.enableUsernameCheck;
    }

    public void setEnableUsernameCheck(Boolean enableUsernameCheck)
    {
        this.enableUsernameCheck = enableUsernameCheck;
    }

    public Integer getMinUsernameLength()
    {
        return this.minUsernameLength;
    }

    public void setMinUsernameLength(Integer minUsernameLength)
    {
        this.minUsernameLength = minUsernameLength;
    }

    public Integer getMaxUsernameLength()
    {
        return this.maxUsernameLength;
    }

    public void setMaxUsernameLength(Integer maxUsernameLength)
    {
        this.maxUsernameLength = maxUsernameLength;
    }

    public Boolean getEnableNicknameCheck()
    {
        return this.enableNicknameCheck;
    }

    public void setEnableNicknameCheck(Boolean enableNicknameCheck)
    {
        this.enableNicknameCheck = enableNicknameCheck;
    }

    public Integer getMinNicknameLength()
    {
        return this.minNicknameLength;
    }

    public void setMinNicknameLength(Integer minNicknameLength)
    {
        this.minNicknameLength = minNicknameLength;
    }

    public Integer getMaxNicknameLength()
    {
        return this.maxNicknameLength;
    }

    public void setMaxNicknameLength(Integer maxNicknameLength)
    {
        this.maxNicknameLength = maxNicknameLength;
    }

    public Boolean getEnablePictureCheck()
    {
        return this.enablePictureCheck;
    }

    public void setEnablePictureCheck(Boolean enablePictureCheck)
    {
        this.enablePictureCheck = enablePictureCheck;
    }

    public List<String> getPictureFormats()
    {
        return this.pictureFormats;
    }

    public void setPictureFormats(List<String> pictureFormats)
    {
        this.pictureFormats = pictureFormats;
    }

    public Long getMinPictureSizeOfBytes()
    {
        return this.minPictureSizeOfBytes;
    }

    public void setMinPictureSizeOfBytes(Long minPictureSizeOfBytes)
    {
        this.minPictureSizeOfBytes = minPictureSizeOfBytes;
    }

    public Long getMaxPictureSizeOfBytes()
    {
        return this.maxPictureSizeOfBytes;
    }

    public void setMaxPictureSizeOfBytes(Long maxPictureSizeOfBytes)
    {
        this.maxPictureSizeOfBytes = maxPictureSizeOfBytes;
    }

    public Boolean getEnableEmailCheck()
    {
        return this.enableEmailCheck;
    }

    public void setEnableEmailCheck(Boolean enableEmailCheck)
    {
        this.enableEmailCheck = enableEmailCheck;
    }

    public List<String> getEmailFormats()
    {
        return this.emailFormats;
    }

    public void setEmailFormats(List<String> emailFormats)
    {
        this.emailFormats = emailFormats;
    }

    public List<String> getSpecialCharacters()
    {
        return this.specialCharacters;
    }

    public void setSpecialCharacters(List<String> specialCharacters)
    {
        this.specialCharacters = specialCharacters;
    }
}
