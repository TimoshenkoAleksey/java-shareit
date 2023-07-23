package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFrontend;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;


    private final int from = 0;
    private final int size = 10;
    private final String state = "ALL";

    private final BookingDtoFrontend bookingDtoFrontend = new BookingDtoFrontend(null, LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusMinutes(20));

    private final UserDto userDto = new UserDto(1L, "John", "john.doe@mail.com");

    private final ItemDto itemDto = new ItemDto(null, "NoName", "Деревянная киянка", null,
            null, null, null, null);

    private final BookingDto bookingDto = new BookingDto(1L, userDto, itemDto, null, null, Status.APPROVED);

    @Test
    @SneakyThrows
    void add() {
        when(bookingService.add(1L, bookingDtoFrontend))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDtoFrontend))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), String.class));
    }

    @Test
    @SneakyThrows
    void update() {
        boolean approved = true;
        when(bookingService.update(1L, 1L, approved))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), String.class));
    }

    @Test
    @SneakyThrows
    void findById() {
        when(bookingService.findById(1L, 1L))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), String.class));
    }

    @Test
    @SneakyThrows
    void findAllBookingsByUserId() {
        when(bookingService.findAllBookingsByUserId(1L, state, from, size))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

    @Test
    @SneakyThrows
    void findAllBookingsByItemOwnerId() {
        when(bookingService.findAllBookingsByItemOwnerId(1L, state, from, size))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class));
    }

}
