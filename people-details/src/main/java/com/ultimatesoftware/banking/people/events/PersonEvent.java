package com.ultimatesoftware.banking.people.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class PersonEvent {
    private String id;
}
