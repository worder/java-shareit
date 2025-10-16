package ru.practicum.shareit.feature.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exception.InternalErrorException;
import ru.practicum.shareit.feature.user.User;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<Long, User> storage = new HashMap<>();
    Long nextId = 0L;

    @Override
    public User create(User user) {
        long id = ++nextId;
        User newUser = user.toBuilder().id(id).build();
        storage.put(id, newUser);
        return newUser;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (storage.containsKey(id)) {
            return Optional.of(storage.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User update(User user) {
        if (storage.containsKey(user.getId())) {
            User updatedUser = user.toBuilder().build();
            storage.put(user.getId(), updatedUser);
            return updatedUser;
        }

        throw new InternalErrorException("User not found");
    }

    @Override
    public void delete(Long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return;
        }

        throw new InternalErrorException("User not found");
    }
}
