package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long userId, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerId(long userId, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(long userId, LocalDateTime startTime,
                                                                 LocalDateTime endTime, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long userId, LocalDateTime startTime,
                                                                    LocalDateTime endTime, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndIsBefore(long userId, LocalDateTime time, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(long userId, LocalDateTime time, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartIsAfter(long userId, LocalDateTime time, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(long userId, LocalDateTime time, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStatus(long userId, Status status, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStatus(long itemId, Status status, PageRequest pageRequest);

    Booking findFirstByItemIdAndStartBeforeAndStatus(long itemId, LocalDateTime time, Status status, Sort sort);

    Booking findFirstByItemIdAndStartAfterAndStatus(long itemId, LocalDateTime time, Status status, Sort sort);

    Booking findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(long itemId, long userId,
                                                                LocalDateTime time, Status status);

    List<Booking> findAllByItemIn(List<Item> items);
}
