package ru.practicum.shareit.feature.item.dal;

import ru.practicum.shareit.feature.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findById(Long id);

    boolean existsById(Long id);

    List<Item> findByOwnerId(Long id);

    List<Item> findByNameOrDescription(Long ownerId, String text);

    Item save(Item item);

    void deleteById(Long id);
}
