package ru.practicum.shareit.feature.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exception.InternalErrorException;
import ru.practicum.shareit.feature.user.model.User;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<Long, User> storage = new HashMap<>();
    private Long nextId = 0L;

    @Override
    public Optional<User> findById(Long id) {
        if (storage.containsKey(id)) {
            return Optional.of(storage.get(id));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (storage.containsKey(user.getId())) {
            storage.put(user.getId(), user);
        } else {
            long id = ++nextId;
            user.setId(id);
            storage.put(id, user);
        }
        return user;
    }

    @Override
    public void deleteById(Long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return;
        }

        throw new InternalErrorException("User not found");
    }
}
