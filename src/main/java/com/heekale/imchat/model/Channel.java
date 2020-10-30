package com.heekale.imchat.model;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author zhenz
 */
@Data
@Document
public class Channel {
    @Id
    String id;
    String name;
    List<String> userIds;
    List<Message> messages;
    Long createOn = DateTime.now().getMillis();

}
