package com.heekale.imchat.handler;

import com.heekale.imchat.model.Channel;
import com.heekale.imchat.model.Message;
import com.heekale.imchat.repository.MessageRepository;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;


/**
 * @author zhenz
 */
@Component
public class MessageStreamHandler {
    final MessageRepository repository;
    final ReactiveMongoTemplate template;

    @PostConstruct
    void configure() {
        template.dropCollection(Message.class).then(
                template.createCollection(Message.class, CollectionOptions.empty().capped().size(4096).maxDocuments(99999))
        ).subscribe();
    }

    public MessageStreamHandler(MessageRepository repository, ReactiveMongoTemplate template) {
        this.template = template;
        this.repository = repository;
    }

    public Flux<Message> getMessage(String receiverId) {
        return repository.findWithTailableCursorByToAndAvailable(receiverId, true);
    }

    public Mono<Message> deleteMessage(String deleteId) {
        return repository.findById(deleteId).flatMap(message -> {
            message.setAvailable(false);
            return repository.save(message);
        });
    }


    @Transactional(rollbackFor = {})
    public Mono<Message> sendMessage(Message message) {
        return repository.save(message);
    }


}
