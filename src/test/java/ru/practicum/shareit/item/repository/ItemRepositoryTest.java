package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRepository repository;

    private final User owner = new User(null, "John", "john.doe@mail.com");
    private final User requester = new User(null, "Алексей", "alexey.timoshenko@mail.com");
    private final ItemRequest itemRequest = new ItemRequest(null, "Деревянная киянка", requester,
            LocalDateTime.now());
    private final Item item = new Item(null, "Мопед", "Железный конь", true, owner,
            itemRequest);
    private final int from = 0;
    private final int size = 10;
    private final PageRequest pageRequest = PageRequest.of(from, size);

    @BeforeEach
    void setUp() {
        manager.persist(requester);
        manager.persist(owner);
        manager.persist(itemRequest);
        manager.persist(item);
    }

    @Test
    void contextLoads() {
        assertNotNull(manager);
    }

    @Test
    void findAllByOwnerId() {
        List<Item> items = repository.findAllByOwnerId(owner.getId(), pageRequest);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(items.get(0).getId(), item.getId());
        assertEquals(items.get(0).getName(), item.getName());
        assertEquals(items.get(0).getDescription(), item.getDescription());
    }

    @Test
    void getItemsByText() {
        String text = "желе";
        List<Item> items = repository.getItemsByText(text, pageRequest);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(items.get(0).getId(), item.getId());
        assertEquals(items.get(0).getName(), item.getName());
        assertEquals(items.get(0).getDescription(), item.getDescription());
    }

    @Test
    void findAllByRequestIdIn() {
        List<Long> requestIds = List.of(itemRequest.getId());
        List<Item> items = repository.findAllByRequestIdIn(requestIds);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(items.get(0).getId(), item.getId());
        assertEquals(items.get(0).getName(), item.getName());
        assertEquals(items.get(0).getDescription(), item.getDescription());
    }

    @Test
    void findAllByRequestId() {
        List<Item> items = repository.findAllByRequestId(itemRequest.getId());

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(items.get(0).getId(), item.getId());
        assertEquals(items.get(0).getName(), item.getName());
        assertEquals(items.get(0).getDescription(), item.getDescription());
    }

}
