package com.scaler.controllers;

import com.scaler.models.Message;
import com.scaler.models.Server;
import com.scaler.models.Subscription;
import com.scaler.models.Topic;
import com.scaler.services.MessageQueueService;

import java.util.concurrent.Future;

public class MessageQueue {
    private MessageQueueService messageQueueService;

    public MessageQueue(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }


    public Future<Void> publish(Topic topic, Message message) {
        return messageQueueService.publish(topic, message);
    }

    public Future<Void> subscribe(Subscription subscription, Topic topic) {
        return messageQueueService.subscribe(subscription, topic);
    }

}
