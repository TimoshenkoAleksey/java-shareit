package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private BookingRepository repository;

    private final User owner = new User(null, "John", "john.doe@mail.com");
    private final User booker = new User(null, "Алексей", "alexey.timoshenko@mail.com");
    private final Item item = new Item(null, "Мопед", "Железный конь", true, owner,
            null);
    private final Booking booking = new Booking(null, LocalDateTime.now().minusMinutes(120),
            LocalDateTime.now().minusMinutes(60), item, booker, Status.APPROVED);
    private final int from = 0;
    private final int size = 10;
    private final PageRequest pageRequest = PageRequest.of(from, size);
    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @BeforeEach
    void setUp() {
        manager.persist(booker);
        manager.persist(owner);
        manager.persist(item);
        manager.persist(booking);

    }

    @Test
    void contextLoads() {
        assertNotNull(manager);
    }

    @Test
    void findAllByBookerId() {
        List<Booking> bookings = repository.findAllByBookerId(booker.getId(), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByItemOwnerId() {
        List<Booking> bookings = repository.findAllByItemOwnerId(owner.getId(), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByBookerIdAndStartIsBeforeAndEndIsAfter() {
        List<Booking> bookings = repository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(booker.getId(),
                LocalDateTime.now().minusMinutes(60), LocalDateTime.now().minusMinutes(120), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter() {
        List<Booking> bookings = repository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(owner.getId(),
                LocalDateTime.now().minusMinutes(60), LocalDateTime.now().minusMinutes(120), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByBookerIdAndEndIsBefore() {
        List<Booking> bookings = repository.findAllByBookerIdAndEndIsBefore(booker.getId(),
                LocalDateTime.now(), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByItemOwnerIdAndEndIsBefore() {
        List<Booking> bookings = repository.findAllByItemOwnerIdAndEndIsBefore(owner.getId(),
                LocalDateTime.now(), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByBookerIdAndStartIsAfter() {
        List<Booking> bookings = repository.findAllByBookerIdAndStartIsAfter(booker.getId(),
                LocalDateTime.now().minusMinutes(180), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByItemOwnerIdAndStartIsAfter() {
        List<Booking> bookings = repository.findAllByItemOwnerIdAndStartIsAfter(owner.getId(),
                LocalDateTime.now().minusMinutes(180), pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByBookerIdAndStatus() {
        List<Booking> bookings = repository.findAllByBookerIdAndStatus(booker.getId(), Status.APPROVED, pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findAllByItemOwnerIdAndStatus() {
        List<Booking> bookings = repository.findAllByItemOwnerIdAndStatus(owner.getId(), Status.APPROVED, pageRequest);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatus() {
        Booking actualBooking = repository.findFirstByItemIdAndStartBeforeAndStatus(item.getId(), LocalDateTime.now(),
                Status.APPROVED, sort);

        assertNotNull(actualBooking);
        assertEquals(actualBooking.getId(), booking.getId());
        assertEquals(actualBooking.getBooker(), booking.getBooker());
        assertEquals(actualBooking.getItem(), booking.getItem());
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatus() {
        Booking actualBooking = repository.findFirstByItemIdAndStartAfterAndStatus(item.getId(),
                LocalDateTime.now().minusMinutes(180), Status.APPROVED, sort);

        assertNotNull(actualBooking);
        assertEquals(actualBooking.getId(), booking.getId());
        assertEquals(actualBooking.getBooker(), booking.getBooker());
        assertEquals(actualBooking.getItem(), booking.getItem());
    }

    @Test
    void findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus() {
        Booking actualBooking = repository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(item.getId(),
                booker.getId(), LocalDateTime.now(), Status.APPROVED);

        assertNotNull(actualBooking);
        assertEquals(actualBooking.getId(), booking.getId());
        assertEquals(actualBooking.getBooker(), booking.getBooker());
        assertEquals(actualBooking.getItem(), booking.getItem());
    }

    @Test
    void findAllByItemIn() {
        List<Item> items = List.of(item);
        List<Booking> bookings = repository.findAllByItemIn(items);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), booking.getId());
        assertEquals(bookings.get(0).getBooker(), booking.getBooker());
        assertEquals(bookings.get(0).getItem(), booking.getItem());
    }

}
