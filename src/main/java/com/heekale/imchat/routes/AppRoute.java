package com.heekale.imchat.routes;

import com.heekale.imchat.handler.ChannelHandler;
import com.heekale.imchat.handler.MessageStreamHandler;
import com.heekale.imchat.model.Channel;
import com.heekale.imchat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api")
final class AppRoute {
    final MessageStreamHandler messageHandler;
    final ChannelHandler channelHandler;

    public AppRoute(MessageStreamHandler messageHandler, ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
        this.messageHandler = messageHandler;
    }

    @GetMapping(value = "/user/message/{uid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamMessages(@PathVariable("uid") String userid) {
        return messageHandler.getMessage(userid);
    }

    @PostMapping(value = "message", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Message> sendMessage(@RequestBody Message message) {
        return messageHandler.sendMessage(message);
    }

    @DeleteMapping(value = "message/{mid}")
    public Mono<Message> deleteMessage(@PathVariable("mid") String messageId) {
        return messageHandler.deleteMessage(messageId);
    }

    @GetMapping(value = "/channel/{uid}")
    Flux<Channel> selfChannels(@PathVariable String uid) {
        return channelHandler.getSelfChannels(uid);
    }

    @PostMapping(value = "/channel")
    Mono<Channel> create(@RequestBody Channel channel) {
        return channelHandler.createChannel(channel);
    }

    @PostMapping(value = "/channel/{cid}/chat")
    Mono addMessage(@PathVariable String cid, @RequestBody Message message) {
        channelHandler.addMessage(cid, message).subscribe();
        return Mono.just("ok");
    }

}