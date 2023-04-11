package com.scaler.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Topic {
    private Long id;
    private String name;
    private List<Message> messages;
}
