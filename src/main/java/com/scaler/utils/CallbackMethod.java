package com.scaler.utils;

import com.scaler.models.Message;
import com.scaler.models.Subscription;

public interface CallbackMethod {
    void call(Subscription subscription,
                     Message message);
}
