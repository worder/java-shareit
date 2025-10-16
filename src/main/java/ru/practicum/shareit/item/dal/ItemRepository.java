package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findById(Long id);

    List<Item> findByOwnerId(Long id);

    List<Item> findByNameOrDescription(Long ownerId, String text);

    Item create(Item item);

    Item update(Item item);

    void delete(Long id);
}
