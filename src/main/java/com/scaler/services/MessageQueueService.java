package com.scaler.services;

import com.scaler.controllers.MessageQueue;
import com.scaler.exception.RetryLimitExhaustedException;
import com.scaler.models.Message;
import com.scaler.models.Server;
import com.scaler.models.Subscription;
import com.scaler.models.Topic;
import com.scaler.strategies.retrystrategy.ExponentialBackoffRetryStrategy;
import com.scaler.strategies.retrystrategy.RetryStrategy;
import com.scaler.utils.StripedExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class MessageQueueService {
    private StripedExecutor stripedExecutor;
    private int numberOfThreads = 16;
    private int maxRetryAttempts = 4;
    private Map<Topic, List<Subscription>> subscriptionMap;
    private RetryStrategy retryStrategy;
    private MessageQueue dlq;
    private Topic dlqErrorTopic;


    public MessageQueueService() {
        stripedExecutor = new StripedExecutor(numberOfThreads);
        subscriptionMap = new ConcurrentHashMap<>();
        this.retryStrategy = new ExponentialBackoffRetryStrategy();
        this.dlq = new MessageQueue(new MessageQueueService());
        this.dlqErrorTopic = new Topic();
    }

    private void publishMessageToQueue(Topic topic, Message message) {
        topic.getMessages().add(message);
        for (Subscription subscription: subscriptionMap.get(topic)) {
            try {
                retryStrategy.push(
                        subscription,
                        message,
                        maxRetryAttempts
                );
            } catch (RetryLimitExhaustedException exception) {
                dlq.publish(topic, message);
            } finally {
                subscription.getOffset().incrementAndGet();
            }

        }
    }

    public Future<Void> publish(Topic topic, Message message) {
        return stripedExecutor.submit(
                topic.getId().hashCode() % numberOfThreads,
                () -> publishMessageToQueue(topic, message)
        );
    }

    private void handleSubscription(Subscription subscription, Topic topic) {
        subscriptionMap.get(topic).add(subscription);
        for (Message message: topic.getMessages()) {
            subscription.getCallback().call(
                    subscription, message
            );
            subscription.getOffset().incrementAndGet();
        }
    }

    public Future<Void> subscribe(Subscription subscription, Topic topic) {
        return stripedExecutor.submit(
                topic.getId().hashCode() % numberOfThreads,
                () -> handleSubscription(subscription, topic)
        );
    }
}
