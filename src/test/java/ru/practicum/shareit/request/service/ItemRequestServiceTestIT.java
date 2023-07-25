package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemRequestServiceTestIT {

    private final EntityManager manager;
    private final ItemRequestService service;

    private final User requester = new User(null, "John", "john.doe@mail.com");
    private final User owner = new User(null, "Алексей", "alexey.timoshenko@mail.com");
    private final ItemRequest request = new ItemRequest(null, "Самогон", requester, LocalDateTime.now());
    private final Item item = new Item(null, "Мопед", "Железный конь", true, owner, request);

    @BeforeEach
    void setUp() {
        manager.persist(requester);
        manager.persist(owner);
        manager.persist(request);
        manager.persist(item);
    }

    @Test
    void getByUserId() {
        List<ItemRequestDto> requests = service.getByUserId(requester.getId());

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertNotNull(requests.get(0).getItems());
        assertEquals(requests.get(0).getId(), request.getId());
        assertEquals(requests.get(0).getDescription(), request.getDescription());
    }
}
