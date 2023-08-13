package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Valid valid;

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(format("Пользователя с id = %s нет в базе", id)));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = validationBeforeUpdate(id, userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private User validationBeforeUpdate(long id, UserDto userDto) {
        User user = valid.checkUser(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (!user.getEmail().equals(userDto.getEmail()) && isEmailPresentInRepository(UserMapper
                    .toUser(userDto))) {
                throw new EmailAlreadyExistsException(String.format("Email %s уже есть в базе у другого пользователя",
                        userDto.getEmail()));
            }
            user.setEmail(userDto.getEmail());
        }
        return user;
    }

    private boolean isEmailPresentInRepository(User user) {
        boolean isPresent = false;
        for (User otherUser : userRepository.findAll()) {
            if (otherUser.getEmail().equals(user.getEmail())) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }
}
