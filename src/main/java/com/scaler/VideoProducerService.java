package com.scaler;

import com.scaler.models.Subscription;

public class VideoProducerService {

    private void transformVideo() {}

    void videoUploaded() {
        Subscription subscription = new Subscription();
        subscription.setCallback(
                (subscriptionFromQueue, messageFromQueue) -> {
                       transformVideo();
                }
        );
    }
}
