package com.sparta.springusersetting.domain.notification.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "system")
public class SystemProperties {

    private String slackToken;
    private String slackChannelId;
    private boolean errorFlag;

    public String getSlackToken() {
        return slackToken;
    }
    public void setSlackToken(String slackToken) {
        this.slackToken = slackToken;
    }
    public String getSlackChannelId() {
        return slackChannelId;
    }
    public void setSlackChannelId(String slackChannelId) {
        this.slackChannelId = slackChannelId;
    }
    public boolean isErrorFlag() {
        return errorFlag;
    }
    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

}

