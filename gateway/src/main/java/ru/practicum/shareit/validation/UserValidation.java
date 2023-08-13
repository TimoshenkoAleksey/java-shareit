package ru.practicum.shareit.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserValidation {

    public void validationBeforeAddAndUpdate(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email не должен быть пустым");
        }
        if (userDto.getName() == null) {
            throw new ValidationException("Имя пользователя не должно быть пустым");
        }
    }
}
