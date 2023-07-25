package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRequestRepository repository;

    private final User requester = new User(null, "John", "john.doe@mail.com");
    private final ItemRequest itemRequest = new ItemRequest(null, "Деревянная киянка", requester,
            LocalDateTime.now());
    private final Sort sort = Sort.by(Sort.Direction.DESC, "created");
    private final int from = 0;
    private final int size = 10;
    private final PageRequest pageRequest = PageRequest.of(from / size, size, sort);

    @Test
    void contextLoads() {
        assertNotNull(manager);
    }

    @Test
    void findAllByRequesterId() {
        manager.persist(requester);
        manager.persist(itemRequest);

        List<ItemRequest> itemRequests = repository.findAllByRequesterId(requester.getId());

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(itemRequests.get(0).getRequester(), itemRequest.getRequester());
        assertEquals(itemRequests.get(0).getDescription(), itemRequest.getDescription());
    }

    @Test
    void findAllByRequesterIdNot() {
        manager.persist(requester);
        manager.persist(itemRequest);

        List<ItemRequest> itemRequestsEmpty = repository.findAllByRequesterIdNot(itemRequest.getId(), pageRequest);

        assertNotNull(itemRequestsEmpty);
        assertEquals(0, itemRequestsEmpty.size());

        List<ItemRequest> itemRequests = repository.findAllByRequesterIdNot(2L, pageRequest);

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(itemRequests.get(0).getRequester(), itemRequest.getRequester());
        assertEquals(itemRequests.get(0).getDescription(), itemRequest.getDescription());
    }

}
