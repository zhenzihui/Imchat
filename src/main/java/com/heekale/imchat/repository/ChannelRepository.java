package com.heekale.imchat.repository;

import com.heekale.imchat.model.Channel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zhenz
 */
public interface ChannelRepository extends ReactiveCrudRepository<Channel, String> {
    /**
     * @param userid
     * @return all self userid
     */
    Flux<Channel> getAllByUserIdsContains(String userid);

}
