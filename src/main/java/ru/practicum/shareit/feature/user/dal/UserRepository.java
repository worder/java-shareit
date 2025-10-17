package ru.practicum.shareit.feature.user.dal;

import ru.practicum.shareit.feature.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Boolean existsById(Long id);

    Optional<User> findByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(Long id);
}
