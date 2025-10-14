package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Override
    public UserDto create(CreateUserRequest request) {
        User user = UserMapper.mapToModel(request);
        this.validateEmail(user);

        return UserMapper.mapToDto(repo.create(user));
    }

    @Override
    public UserDto read(Long id) {
        return repo.findById(id)
                .map(UserMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserDto update(Long id, UpdateUserRequest request) {
        User currentUser = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        User updatedUser = UserMapper.updateModelFields(currentUser, request);
        this.validateEmail(updatedUser);

        return UserMapper.mapToDto(repo.update(updatedUser));
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    private void validateEmail(User user) {
        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(user.getId())) {
                throw new BadRequestException("Email already exists");
            }
        });
    }


}
