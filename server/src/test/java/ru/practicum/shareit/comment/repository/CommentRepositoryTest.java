package ru.practicum.shareit.comment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private CommentRepository repository;

    private final User owner = new User(null, "John", "john.doe@mail.com");
    private final User author = new User(null, "Алексей", "alexey.timoshenko@mail.com");
    private final Item item = new Item(null, "Мопед", "Железный конь", true, owner,
            null);
    private final Comment comment = new Comment(null, "Очень полезная и нужная шняга!", author, item,
            LocalDateTime.now());

    @BeforeEach
    void setUp() {
        manager.persist(author);
        manager.persist(owner);
        manager.persist(item);
        manager.persist(comment);

    }

    @Test
    void contextLoads() {
        assertNotNull(manager);
    }

    @Test
    void findAllByItemId() {
        List<Comment> comments = repository.findAllByItemId(item.getId());

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comments.get(0).getId(), comment.getId());
        assertEquals(comments.get(0).getText(), comment.getText());
        assertEquals(comments.get(0).getAuthor(), comment.getAuthor());
        assertEquals(comments.get(0).getItem(), comment.getItem());
    }

    @Test
    void findAllByItemIn() {
        List<Item> items = List.of(item);
        List<Comment> comments = repository.findAllByItemIn(items);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comments.get(0).getId(), comment.getId());
        assertEquals(comments.get(0).getText(), comment.getText());
        assertEquals(comments.get(0).getAuthor(), comment.getAuthor());
        assertEquals(comments.get(0).getItem(), comment.getItem());
    }

}
