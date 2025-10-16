package ru.practicum.shareit.feature.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ConflictEntity;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.user.User;
import ru.practicum.shareit.feature.user.dal.UserRepository;
import ru.practicum.shareit.feature.user.dto.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.UpdateUserRequest;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.dto.UserMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Override
    public UserDto create(CreateUserRequest request) {
        User user = UserMapper.mapToModel(request);
        this.validateEmail(user);

        User createdUser = repo.create(user);
        log.info("Created user: {} from request: {}", createdUser, request);
        return UserMapper.mapToDto(createdUser);
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

        updatedUser = repo.update(updatedUser);
        log.info("Updated user: {} from request: {}", updatedUser, request);
        return UserMapper.mapToDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        User currentUser = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        repo.delete(id);
        log.info("Deleted user: {}", currentUser);
    }

    @Override
    public boolean isUserExists(Long id) {
        return repo.findById(id).isPresent();
    }

    private void validateEmail(User user) {
        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(user.getId())) {
                throw new ConflictEntity("Email already exists");
            }
        });
    }


}
