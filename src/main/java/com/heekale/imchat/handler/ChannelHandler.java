package com.heekale.imchat.handler;

import com.heekale.imchat.model.Channel;
import com.heekale.imchat.model.Message;
import com.heekale.imchat.repository.ChannelRepository;
import com.heekale.imchat.repository.MessageRepository;
import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhenz
 */
@Component
public class ChannelHandler {
    final ChannelRepository repository;
    final MessageRepository messageRepository;

    public ChannelHandler(ChannelRepository repository, MessageRepository messageRepository) {
        this.repository = repository;
        this.messageRepository = messageRepository;
    }

    public Mono<Channel> createChannel(Channel channel) {
        return repository.save(channel);
    }

    public Mono<List<Message>> addMessage(String channelId, Message message) {
        return repository.findById(channelId).flatMap(channel -> {
           List<Message> messageList = channel.getMessages() == null? new ArrayList<>(): channel.getMessages();
           messageList.add(message);
           channel.setMessages(messageList);
           repository.save(channel).subscribe();
           val toNotify = channel.getUserIds().stream().map(user -> {
               Message msg = new Message();
               msg.setContent(message.getContent());
               msg.setAvailable(message.isAvailable());
               msg.setCreateOn(message.getCreateOn());
               msg.setSender(message.getSender());
               msg.setChannelId(message.getChannelId());
               msg.setTo(user);
               return msg;
           }).collect(Collectors.toList());
           return Mono.just(toNotify);
        }).doOnNext(messages -> {
            System.out.println(messages);
            messageRepository.saveAll(messages).subscribe();
        });
    }

    public Flux<Channel> getChannels() {
        return repository.findAll();
    }

    public Flux<Channel> getSelfChannels(String uid) {
        return repository.getAllByUserIdsContains(uid);

    }


}
