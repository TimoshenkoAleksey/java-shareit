package ru.practicum.shareit.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemValidation {

    public void validationBeforeAdd(ItemDto itemDto) {
        if (itemDto.getName().isEmpty()) {
            throw new ValidationException(("Имя вещи не должно быть пустым."));
        } else if (itemDto.getDescription() == null) {
            throw new ValidationException("Описание вещи не должно быть пустым.");
        } else if (itemDto.getAvailable() == null) {
            throw new ValidationException("Нужно указать, доступна ли вещь.");
        }
    }

    public void validationBeforeUpdate(ItemDto itemDto) {
        if (itemDto.getName() != null && itemDto.getName().isBlank()) {
            throw new ValidationException("Имя вещи не должно быть пустым.");
        }
        if (itemDto.getDescription() != null && itemDto.getDescription().isBlank()) {
            throw new ValidationException("Описание вещи не должно быть пустым.");
        }
    }
}
