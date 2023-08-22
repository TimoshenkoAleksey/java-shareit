package ru.practicum.shareit.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.error.ValidationException;

@Service
public class BookingValidation {

    public void checkDate(BookItemRequestDto bookingItemRequestDto) {
        if (bookingItemRequestDto.getEnd().isBefore(bookingItemRequestDto.getStart())) {
            throw new ValidationException("Время начала бронирования вещи не должна быть позже времени " +
                    "окончания бронирования");
        }
        if (bookingItemRequestDto.getEnd().isEqual(bookingItemRequestDto.getStart())) {
            throw new ValidationException("Время начала и окончания бронирования вещи не должны совпадать");
        }
    }
}
