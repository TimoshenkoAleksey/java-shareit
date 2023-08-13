package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFrontend;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody BookingDtoFrontend bookingDtoFrontend) {
        return bookingService.add(userId, bookingDtoFrontend);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam String state,
                                                    @RequestParam int from,
                                                    @RequestParam int size) {
        return bookingService.findAllBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingsByItemOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam String state,
                                                         @RequestParam int from,
                                                         @RequestParam int size) {
        return bookingService.findAllBookingsByItemOwnerId(userId, state, from, size);
    }
}
