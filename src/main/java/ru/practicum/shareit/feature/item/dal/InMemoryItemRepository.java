package ru.practicum.shareit.feature.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exception.InternalErrorException;
import ru.practicum.shareit.feature.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final HashMap<Long, Item> storage = new HashMap<>();
    private Long lastId = 0L;

    @Override
    public Optional<Item> findById(Long id) {
        if (storage.containsKey(id)) {
            return Optional.of(storage.get(id));
        }

        return Optional.empty();
    }

    @Override
    public List<Item> findByOwnerId(Long id) {
        return storage.values().stream()
                .filter(i -> i.getOwner().equals(id))
                .toList();
    }

    @Override
    public List<Item> findByNameOrDescription(Long ownerId, String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return this.findByOwnerId(ownerId).stream()
                .filter(i -> i.getAvailable() == true
                        && i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())
                )
                .toList();
    }

    @Override
    public Item create(Item item) {
        Long id = ++lastId;
        Item newItem = item.toBuilder().id(id).build();
        storage.put(id, newItem);
        return newItem;
    }

    @Override
    public Item update(Item item) {
        if (storage.containsKey(item.getId())) {
            Item updatedItem = item.toBuilder().build();
            storage.put(item.getId(), updatedItem);
            return updatedItem;
        }

        throw new InternalErrorException("Item not found");
    }

    @Override
    public void delete(Long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return;
        }

        throw new InternalErrorException("Item not found");
    }
}
