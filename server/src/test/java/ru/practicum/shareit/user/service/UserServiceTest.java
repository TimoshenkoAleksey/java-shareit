package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.Valid;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    Valid valid;
    @InjectMocks
    UserServiceImpl userService;

    private final User user = new User(1L, "John", "john.doe@mail.com");

    @Test
    void findAllUsersTest() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> userDtoList = userService.findAllUsers();

        assertNotNull(userDtoList);
        assertEquals(1, userDtoList.size());
        assertEquals(user.getId(), userDtoList.get(0).getId());
        assertEquals(user.getName(), userDtoList.get(0).getName());
        assertEquals(user.getEmail(), userDtoList.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUserByIdTest() {
        Long userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.findUserById(userId);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void findUserByIdWithoutUser() {
        Long userId = user.getId();
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.findUserById(userId));
    }

    @Test
    void addUserTest() {
        when(userRepository.save(user))
                .thenReturn(user);

        UserDto userDto = userService.addUser(UserMapper.toUserDto(user));

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addUserWithNullEmail() {
        user.setEmail(null);

        assertThrows(NullPointerException.class,
                () -> userService.addUser(UserMapper.toUserDto(user)));
    }

    @Test
    void addUserWithNullName() {
        user.setName(null);

        assertThrows(NullPointerException.class,
                () -> userService.addUser(UserMapper.toUserDto(user)));
    }

    @Test
    void updateUserTest() {
        Long userId = user.getId();
        when(userRepository.save(user)).thenReturn(user);
        when(valid.checkUser(userId)).thenReturn(user);

        UserDto userDto = userService.updateUser(userId, UserMapper.toUserDto(user));

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void updateUserWithEmptyName() {
        Long userId = user.getId();
        user.setName("");
        when(valid.checkUser(userId)).thenReturn(user);

        assertThrows(NullPointerException.class,
                () -> userService.updateUser(userId, UserMapper.toUserDto(user)));
    }

    @Test
    void deleteUserTest() {
        Long userId = user.getId();

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
