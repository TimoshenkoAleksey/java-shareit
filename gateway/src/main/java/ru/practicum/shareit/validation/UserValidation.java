package ru.practicum.shareit.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserValidation {

    public void validationBeforeAdd(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email не должен быть пустым");
        }
        if (userDto.getName() == null) {
            throw new ValidationException("Имя пользователя не должно быть пустым");
        }
    }

    public void validationBeforeUpdate(UserDto userDto) {
        if (userDto.getName() != null && userDto.getName().isBlank()) {
            throw new ValidationException("Имя пользователя не должно быть пустым");
        }
        if (userDto.getEmail() != null && userDto.getEmail().isBlank()) {
            throw new ValidationException("Email не должен быть пустым");
        }
    }
}
