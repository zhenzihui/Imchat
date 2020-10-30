package com.heekale.imchat.repository;

import com.heekale.imchat.model.Message;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * @author zhenz
 */

public interface MessageRepository extends ReactiveCrudRepository<Message, String> {


    /**
     * @param userId
     * @return stream message
     */
    @Tailable
    Flux<Message> findWithTailableCursorByTo(String userId);
    @Tailable
    Flux<Message> findWithTailableCursorByToAndAvailable(String userId, boolean available);


}
