package com.ovo307000.bigevent.global.surety.checker.input;

import com.ovo307000.bigevent.global.excaption.EmailFormatException;
import com.ovo307000.bigevent.global.excaption.NicknameInvalidException;
import com.ovo307000.bigevent.global.properties.RegisterProperties;
import org.springframework.stereotype.Component;

@Component("userChecker")
public class UserChecker
{
    private final RegisterProperties registerProperties;

    public UserChecker(RegisterProperties registerProperties)
    {
        this.registerProperties = registerProperties;
    }

    public Exception checkNickname(String nickname)
    {
        if (nickname == null || nickname.isEmpty())
        {
            return new NicknameInvalidException("Nickname is empty");
        }

        if (! this.registerProperties.getEnableNicknameCheck())
        {
            return null;
        }

        if (nickname.length() < this.registerProperties.getMinNicknameLength())
        {
            return new NicknameInvalidException("Nickname is too short");
        }
        if (nickname.length() > this.registerProperties.getMaxNicknameLength())
        {
            return new NicknameInvalidException("Nickname is too long");
        }

        return null;
    }

    public Exception checkUsername(String username)
    {
        if (username == null || username.isEmpty())
        {
            return new NicknameInvalidException("Username is empty");
        }

        if (! this.registerProperties.getEnableUsernameCheck())
        {
            return null;
        }

        if (username.length() < this.registerProperties.getMinUsernameLength())
        {
            return new NicknameInvalidException("Username is too short");
        }
        if (username.length() > this.registerProperties.getMaxUsernameLength())
        {
            return new NicknameInvalidException("Username is too long");
        }

        return null;
    }

    public Exception checkPassword(String password)
    {
        if (password == null || password.isEmpty())
        {
            return new IllegalArgumentException("Password is empty");
        }

        if (! this.registerProperties.getEnablePasswordCheck())
        {
            return null;
        }

        if (password.length() < this.registerProperties.getMinPasswordLength())
        {
            return new IllegalArgumentException("Password is too short");
        }
        if (password.length() > this.registerProperties.getMaxPasswordLength())
        {
            return new IllegalArgumentException("Password is too long");
        }
        if (! password.matches(this.registerProperties.getPasswordRegex()))
        {
            return new IllegalArgumentException("Password is invalid");
        }

        return null;
    }

    public Exception checkUserPicture(String userPicture)
    {
        if (userPicture == null || userPicture.isEmpty())
        {
            return new IllegalArgumentException("User picture is empty");
        }

        if (! this.registerProperties.getEnablePictureCheck())
        {
            return null;
        }

        if (this.registerProperties.getPictureFormats()
                                   .stream()
                                   .noneMatch(userPicture::endsWith))
        {
            return new IllegalArgumentException("User picture is invalid");
        }
        if (! userPicture.matches("^(http|https)://.*"))
        {
            return new IllegalArgumentException("User picture is invalid");
        }

        // TODO: 检查图片是否存在，并且是否能够访问，大小是否在限制范围内

        return null;
    }

    public Exception checkEmail(String email)
    {
        if (email == null || email.isEmpty())
        {
            return new EmailFormatException("Email is empty");
        }

        if (! this.registerProperties.getEnableEmailCheck())
        {
            return null;
        }

        if (this.registerProperties.getEmailFormats()
                                   .stream()
                                   .noneMatch(email::endsWith))
        {
            return new EmailFormatException("Email is not in whitelist");
        }
        if (! email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
        {
            return new EmailFormatException("Email is invalid");
        }

        return null;
    }
}
