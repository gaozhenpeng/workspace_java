package com.at.rabbitmq.prod;

import java.io.Serializable;

public class MessageInfo implements Serializable {
    private static final long serialVersionUID = 2508532411235035487L;
    //渠道
    private String channel;
    //来源
    private String content;
    public String getChannel() {
        return channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
