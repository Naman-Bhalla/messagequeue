package com.scaler.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Message {
    private Long id;
    private Map<String, String> properties;
}
