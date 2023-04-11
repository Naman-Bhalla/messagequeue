package com.scaler.strategies.retrystrategy;

import com.scaler.exception.RetryLimitExhaustedException;
import com.scaler.models.Message;
import com.scaler.models.Subscription;

public class ExponentialBackoffRetryStrategy implements RetryStrategy {

    @Override
    public void push(Subscription subscription,
                     Message message,
                     int maxAttempts)
            throws RetryLimitExhaustedException, InterruptedException {
        int currentAttempts = 0;
        int previousSleepMillis = 500;
        while (currentAttempts < maxAttempts) {
            try {
                currentAttempts += 1;
                subscription.getCallback().call(
                        subscription, message
                );
                return;
            } catch (Exception e) {
                previousSleepMillis *= 2;
                Thread.sleep(previousSleepMillis);
            }
        }

    }
}
