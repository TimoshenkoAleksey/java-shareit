package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private long genereteId = 1;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(genereteId++);
        users.put(user.getId(), user);
        log.info("Добавден новый пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        users.put(id, user);
        log.info("Обновлен пользователь с id {}", user.getId());
        return user;
    }

    @Override
    public boolean deleteUser(long id) {
        users.remove(id);
        log.info("Пользователь с id {} удален", id);
        return true;
    }

    @Override
    public boolean isEmailPresentInRepository(User user) {
        boolean isPresent = false;
        for (User otherUser : users.values()) {
            if (otherUser.getEmail().equals(user.getEmail())) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }
}
