package com.heekale.imchat.model;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zhenz
 */
@Data
@Document(collection = "message")
public final class Message {
    @Id
    private String id;
    private String content;
    private String sender;
    private boolean available = true;
    private String to;
    private String channelId;
    private long createOn = DateTime.now().getMillis();
}