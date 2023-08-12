package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAlreadyExistsException extends RuntimeException {
    private final String parameter;
}
