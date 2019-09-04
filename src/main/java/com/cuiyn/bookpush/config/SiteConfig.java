package com.cuiyn.bookpush.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "site")
public class SiteConfig {
    private String name;
    private Integer pushLimit;
    private String bookDir;
    private String registerCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPushLimit() {
        return pushLimit;
    }

    public void setPushLimit(Integer pushLimit) {
        this.pushLimit = pushLimit;
    }

    public String getBookDir() {
        return bookDir;
    }

    public void setBookDir(String bookDir) {
        this.bookDir = bookDir;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }
}
