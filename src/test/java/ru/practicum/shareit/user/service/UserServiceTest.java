package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.Valid;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void findAllUsers() {
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
    void findUserById() {
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
    void addUser() {
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
    void updateUser() {
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
    void deleteUser() {
        Long userId = user.getId();

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
