package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceIntegrationTest {

    private final EntityManager manager;
    private final UserService service;

    private final User user = new User(null, "John", "john.doe@mail.com");
    private final UserDto userDto = new UserDto(null, "Алексей", "alexey.timoshenko@mail.com");

    @BeforeEach
    void setUp() {
        manager.persist(user);
    }

    @Test
    void updateUser() {
        UserDto updatedUser = service.updateUser(user.getId(), userDto);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getId(), user.getId());
        assertEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), user.getEmail());
    }
}
