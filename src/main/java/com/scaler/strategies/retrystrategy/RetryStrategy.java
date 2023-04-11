package com.scaler.strategies.retrystrategy;

import com.scaler.exception.RetryLimitExhaustedException;
import com.scaler.models.Message;
import com.scaler.models.Subscription;

public interface RetryStrategy {

    void push(Subscription subscription,
              Message message,
              int maxAttempts)
            throws RetryLimitExhaustedException, InterruptedException;
}
