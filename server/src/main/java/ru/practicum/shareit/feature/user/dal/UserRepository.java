package ru.practicum.shareit.feature.user.dal;

import ru.practicum.shareit.feature.user.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    boolean existsById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void deleteById(Long id);
}
