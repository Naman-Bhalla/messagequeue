package com.scaler.models;

import com.scaler.utils.CallbackMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class Subscription {
    private Long id;
    private Topic topic;
    private Server server;
    private AtomicLong offset;
    private CallbackMethod callback;
}
