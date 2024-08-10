package com.ovo307000.bigevent.global.surety.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("propertiesCheck")
public class PropertiesCheck
{
    private static final Logger log = LoggerFactory.getLogger(PropertiesCheck.class);

    public boolean checkPathFormat(List<String> paths)
    {
        log.debug("Checking paths format...");

        return paths.stream()
                    .allMatch(this::checkPathFormat);
    }

    public boolean checkPathFormat(String path)
    {
        log.debug("Checking path format...");

        return path.startsWith("/") && path.endsWith("/");
    }

    public boolean checkEmailFormat(List<String> emails)
    {
        log.debug("Checking emails format...");

        return emails.stream()
                     .allMatch(this::checkEmailFormat);
    }

    public boolean checkEmailFormat(String email)
    {
        log.debug("Checking email format...");

        return email.contains("@");
    }
}
